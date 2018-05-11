<?php
    require('adminCheck.php');
    $possiblePkgs = $_SESSION['pkgList'];

    foreach($possiblePkgs as $pkgNum) {
        if (isset($_POST[$pkgNum])) {
            removePkg($pkgNum);
        }
    }

    #If we're here, we didn't have a package to remove, redirect.

    function removePkg($thePkg) {
        #echo "Time to remove " . $thePkg;
        $dbh = new PDO('mysql:host=localhost;dbname=scannerTool;', 'zstuck', '');
        $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
        #First statement removes the package from the active package list.
        $rmStmtOne = $dbh->prepare("DELETE FROM activepkgs WHERE id = ?");
        if (!$rmStmtOne) {
            die();
        }
        else {
            $rmStmtOne->execute(array($thePkg));
        }
        #Second statement, drop the table for the package.
        $rmStmtTwo = $dbh->prepare("DROP TABLE `$thePkg`");
        if (!$rmStmtTwo) {
            die();
        }
        else {
            $rmStmtTwo->execute();
        }
        #Redirect back to previous page
        header("Location: ./adminDelete.php");
        exit();
    }
?>
