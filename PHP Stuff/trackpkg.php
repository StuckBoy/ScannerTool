<?php
    @$packageNum = $_POST['pkgNum'];
    @$handler = $_POST['username'];
    @$locationData = $_POST['coords'];
    @$timestamp = $_POST['timestamp'];
    $currentPkgs = [];

    $dbh = new PDO('mysql:host=localhost;dbname=scannerTool', 'zstuck', '');
    $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);

    lookupPackage($handler, $packageNum, $locationData, $timestamp, $dbh);

    #Key function, looks up package, and react appropriately
    function lookupPackage($handler, $packageNum, $locationData, $timestamp, $dbh) {
        try {
            $listStmt = $dbh->prepare("SELECT id FROM activepkgs");
            if (!$listStmt) {
                print_r($dbh->errorInfo());
                die();
            }
            else {
                $listStmt->execute();
            }
            while($row = $listStmt->fetch()) {
                $currentPkgs[] = $row[0];
            }
            $stmt = $dbh->prepare("SELECT count(id) FROM activepkgs WHERE id = ?");
            if (!$stmt) {
                echo "Error while looking up in activepkg";
                die();
            }
            if ($stmt->execute(array($packageNum))) {
                #Check under active package, create entry if none exists, then log it.
                $result = $stmt->fetch();
                if ($result[0] == "0") {
                    preg_match('/^[0-9]{8}$/', $packageNum, $match);
                    if ($match[0] == $packageNum) {
                        $currentPkgs[] = $packageNum;
                        createPkgEntry($handler, $packageNum, $dbh);
                        createPkgLog($packageNum, $handler, $locationData, $timestamp, $currentPkgs, $dbh);
                    }
                    else {
                        #$packageNum is not a 8 digit barcode.
                        die();
                    }
                }
                else {
                    #If package has entry and history, log location in associated table.
                    updatePkgHistory($handler, $packageNum, $locationData, $timestamp, $dbh);
                }
                checkForNotifications($dbh, $packageNum);
            }
        }
        catch (Exception $e) {
            print("Error!: " . $e->getMessage() . "<br>");
            echo "Error in lookupPackage.";
            die();
        }
    }

    #Updates history of package in its respective table
    function updatePkgHistory($handler, $pkgNum, $locData, $timestamp, $dbh) {
        $insertStmt = $dbh->prepare("INSERT INTO `$pkgNum` values (?, ?, ?)");
        if (!$insertStmt) {
            print_r($dbh->errorInfo());
            echo "updatePkgHistory failed.";
            die();
        }
        $insertStmt->execute(array($timestamp, $locData, $handler));
        echo "Success";
    }

    #Creates listing under activepkgs for that package
    function createPkgEntry($handler, $packageNum, $dbh) {
        $creatStmt = $dbh->prepare("INSERT INTO activepkgs (id, handler, history) values (?, ?, ?)");
        if (!$creatStmt) {
            print_r($dbh->errorInfo());
            echo "error in createPkgEntry statement.";
            die();
        }
        else {
            $creatStmt->execute(array($packageNum, $handler, "yes"));
        }
    }

    #Creates associated table for storing package location history
    function createPkgLog($pkgNum, $handler, $locData, $timestamp, $currentPkgs, $dbh) {
        if (in_array($pkgNum, $currentPkgs)) {
            $tableStmt = $dbh->prepare("CREATE TABLE `$pkgNum` (timestamp VARCHAR(25), coordinates VARCHAR(50), handler VARCHAR(20))");
            if (!$tableStmt) {
                print_r($dbh->errorInfo());
                echo "Malformed create statement.";
                die();
            }
            $tableStmt->execute();
            updatePkgHistory($handler, $pkgNum, $locData, $timestamp, $dbh);
        }
        else {
            die();
        }
    }

    function checkforNotifications($dbh, $packageNum) {
        $notifyStmt = $dbh->prepare("SELECT owner FROM activepkgs where id=?");
        if (!$notifyStmt) {
            print_r($dbh->errorInfo());
            die();
        }
        else {
            $notifyStmt->execute(array($packageNum));
            $owner = $notifyStmt->fetch();
            $subStmt = $dbh->prepare("SELECT subscriptions, username, email_address FROM customers INNER JOIN activepkgs on customers.username = activepkgs.owner and id = ?");
            if (!$subStmt) {
                print_r($dbh->errorInfo());
                die();
            }
            else {
                $subStmt->execute(array($packageNum));
                $status = $subStmt->fetch();
                $customer = $status[1];
                $address = $status[2];
                switch($status[0]) {
                    case "both":
                        //Both, most complicated.
                        sendMail($dbh, $packageNum, $customer, $address);
                        sendPushNotify($dbh, $packageNum, $customer);
                        break;
                    case "email":
                        //Only send email to user.
                        sendMail($dbh, $packageNum, $customer, $address);
                        break;
                    case "notifications":
                        //Send the user a push notification.
                        sendPushNotify($dbh, $packageNum, $customer);
                        break;
                    case "none":
                        //Do nothing, they haven't subscribed.
                    default:
                        //Do nothing, same as NULL;
                }
            }
        }
    }

    function sendMail($dbh, $packageNum, $customer, $address) {
        $message = "Your package, " . $packageNum . ", has received an update to its location!";
        $message = wordwrap($message, 70, "\r\n");
        $header = "From: zstuck@trackerservice.com\r\n";
        mail($address, "Package Update", $message, $header);
    }

    function sendPushNotify($dbh, $packageNum, $customer) {
        $pushStmt = $dbh->prepare("SELECT token FROM firebasekeys WHERE customer=?");
        if (!$pushStmt) {
            print_r($dbh->errorInfo());
            die();
        }
        else {
            $pushStmt->execute(array($customer));
            $userToken = $pushStmt->fetch();

            $body = [
                "message" => [
                    "notification" => [
                        "title" => "Package Update!",
                        "body" => "Your package, " . $packageNum . ", has updated locations.",
                    ],
                    "token" => $userToken[0],
                ],
            ];

            $jsonBody = json_encode($body);
            $getKeyFile = escapeshellcmd('getKey.py');
            $output = shell_exec($getKeyFile);
            #echo $output;

            exec('python3 getKey.py', $execOutput, $ret_code);

            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL,            "https://fcm.googleapis.com/v1/projects/senior-project-81660/messages:send");
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1 );
            curl_setopt($ch, CURLOPT_POST,           1 );
            curl_setopt($ch, CURLOPT_POSTFIELDS,     $jsonBody);
            curl_setopt($ch, CURLOPT_HTTPHEADER,     array("Content-Type: application/json", "Authorization: Bearer ". $execOutput[0]));

            $result=curl_exec($ch);

            print_r($result);
        }
    }
?>
