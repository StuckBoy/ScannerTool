<?php
    #Get User's credentials, then fetch the salt
    @$user = $_POST['username'];
    @$password = $_POST['password'];
    $dbh = new PDO('mysql:host=localhost;dbname=scannerTool', 'zstuck', '');
    $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
    fetchSalt($user, $password, $dbh);

    function fetchSalt($user, $password, $dbh) {
        try {
            $stmt = $dbh->prepare("SELECT salt FROM scanners WHERE username = ?");
            if (!$stmt) {
                print_r($dbh->errorInfo());
                die();
            }
            if ($stmt->execute(array($user))) {
                $results = $stmt->fetch();
                $salt = $results[0];
                $saltedPass = $salt . $password;
                $pass = hash("sha256", $saltedPass);
                attemptLogin($user, $pass, $dbh);
            }
        }
        catch(Exception $e) {
            print("Error!: " . $e->getMessage() . "<br>");
            die();
        }
    }

    function attemptLogin($user, $pass, $dbh) {
        try {
            $stmt = $dbh->prepare("SELECT count(username) FROM scanners WHERE username = ? and password = ?");
            if (!$stmt) {
                print_r($dbh->errorInfo());
                die();
            }
            if ($stmt->execute(array($user, $pass))) {
                $count = $stmt->fetch();
                if ($count[0] == "1") {
                    echo "Success!";
                }
                else {
                    echo "Error";
                }
            }
        }
        catch (Exception $e) {
            print("Error!: " . $e->getMessage() . "<br>");
            die();
        }
    }
?>
