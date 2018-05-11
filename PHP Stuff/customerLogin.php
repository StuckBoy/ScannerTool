<?php
    @$user = $_POST['username'];
    @$password = $_POST['password'];
    $dbh = new PDO('mysql:host=localhost;dbname=scannerTool', 'zstuck', '');
    $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
    fetchSalt($user, $password, $dbh);

    function fetchSalt($user, $password, $dbh) {
        try {
            $stmt = $dbh->prepare("SELECT salt FROM customers WHERE username = ?");
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
            $stmt = $dbh->prepare("SELECT count(username) FROM customers WHERE username = ? and password = ?");
            if (!$stmt) {
                print_r($dbh->errorInfo());
                die();
            }
            if ($stmt->execute(array($user, $pass))) {
                $count = $stmt->fetch();
                if ($count[0] == "1") {
                    getPreferences($user, $dbh);
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

    function getPreferences($user, $dbh) {
        try {
            $prefStmt = $dbh->prepare("SELECT subscriptions FROM customers WHERE username = ?");
            if (!$prefStmt) {
                print_r($dbh->errorInfo());
                die();
            }
            else {
                $prefStmt->execute(array($user));
                $prefs = $prefStmt->fetch();
                echo "Success! " . $prefs[0];
            }
        }
        catch (Exception $e) {
            print("Error!: " . $e->getMessage() . "<br>");
        }
    }
?>
