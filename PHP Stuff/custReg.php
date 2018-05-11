<?php
    @$username = $_POST['username'];
    @$password = $_POST['pass'];
    @$email = '';
    if (isset($_POST['email'])) {
        $email = $_POST['email'];
    }
    $dbh = new PDO('mysql:host=localhost;dbname=scannerTool', 'zstuck', '');
    $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
    $salt = generateSalt(16);
    $saltPass = hash("sha256", $salt . $password);
    createUser($username, $saltPass, $salt, $email, $dbh);

    function generateSalt($length) {
        $alphaNumPool = array_merge(range(0,9), range('a', 'z'), range('A', 'Z'));
        $key = '';
        for ($i=0; $i < $length; $i++) {
            $key .= $alphaNumPool[mt_rand(0, count($alphaNumPool) - 1)];
        }

        return $key;
    }

    function createUser($user, $pass, $salt, $email, $dbh) {
        try {
            $createStmt = $dbh->prepare("INSERT into customers VALUES (?, ?, ?, ?, ?)");
            if (!$createStmt) {
                print_r($dbh->errorInfo());
                die();
            }
            else {
                $createStmt->execute(array($user, $pass, $salt, 'none', $email));
                sleep(2);
                header("Location: ./webCustomerForm.php");
            }
        }
        catch (Exception $e) {
            print("Error, " . $e->getMessage() . "<br>");
            die();
        }
    }
?>
