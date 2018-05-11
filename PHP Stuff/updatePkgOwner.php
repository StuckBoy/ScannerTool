<?php
    require('adminCheck.php');
    $count = $_SESSION['selectCount'];
    $pkgList = $_SESSION['pkgList'];
    $ownerList = $_SESSION['ownerList'];
    for($a = 0; $a < $count; $a++) {
        if ($ownerList[$a] != $_POST['select' . $a]) {
            try{
                $dbh = new PDO('mysql:host=localhost;dbname=scannerTool;', 'zstuck', '');
                $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
                $updateStmt = $dbh->prepare("update activepkgs set owner = ? where id = ?");
                if (!$updateStmt) {
                    die();
                }
                else {
                    $updateStmt->execute(array($_POST['select' . $a], $pkgList[$a]));
                    sleep(3);
                    header("Location: ./adminOwner.php");
                    exit();
                }
            }
            catch (Exception $e) {
                print_r("Error, " . $e->getMessage() . "<br>");
            }
        }
    }
?>
