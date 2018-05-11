<?php
    @$user = $_POST['username'];
    @$password = $_POST['pass'];
    session_start();
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
                if (isset($_SESSION["admin"]) && $_SESSION["admin"] == "true") {
                    header("Location: ./adminPage.php");
                    exit();
                }
                elseif ($count[0] == "1" && $user == "admin") {
                    $_SESSION["admin"] = "true";
                    header("Location: ./adminPage.php");
                    exit();
                }
                else {
                    header("Location: ./webView.php");
                    exit();
                }
            }
        }
        catch (Exception $e) {
            print("Error!: " . $e->getMessage() . "<br>");
            die();
        }
    }
?>
