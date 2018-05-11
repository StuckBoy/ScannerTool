<?php
    require('adminCheck.php');
    ob_start();
?>
<html>
    <h1>Package Owner Page</h1>
    <form method="post" action="adminPage.php">
        <button type="submit" name="goBack">Go Back</button><br><br>
    </form>
</html>

<?php
    $pkgList = array();
    $customerList = array();
    $ownerList = array();
    $dbh = new PDO('mysql:host=localhost;dbname=scannerTool;', 'zstuck', '');

    try {
        $custStmt = $dbh->prepare("SELECT username FROM customers");
        $custStmt->execute();
        while ($row = $custStmt->fetch()) {
            $customerList[] = $row['username'];
        }
    }
    catch (Exception $e) {
        print_r("Error, " . $e->getMessage() . "<br>");
        die();
    }

    try {
        $activeList = $dbh->prepare("SELECT id, owner FROM activepkgs");
        $activeList->execute();
        $count = 0;
        echo '<form method="post" action="updatePkgOwner.php">';
        while ($row = $activeList->fetch()) {
            echo '<span style="font-size: 18px;">';
            echo $row['id'];
            echo ' <select name="select' . $count . '">';
            foreach ($customerList as $cust) {
                if ($cust == $row['owner']) {
                    echo ' <option value=' . $cust . ' selected>' . $cust . '</option>';
                }
                elseif ($cust != $row['owner']) {
                    echo ' <option value=' . $cust . '>' . $cust . '</option>';
                }
            }
            if ($row['owner'] == "None") {
                echo ' <option value=None selected>None</option>';
            }
            echo '</select>';
            echo '</span><br>';
            $pkgList[] = $row['id'];
            $ownerList[] = $row['owner'];
            $count++;
        }
        echo '<button type="submit">Submit</button>';
        echo "</form>";
        $_SESSION['pkgList'] = $pkgList;
        $_SESSION['ownerList'] = $ownerList;
        $_SESSION['selectCount'] = $count;
    }
    catch (Exception $e) {
        print_r("Error, " . $e->getMessage() . "<br>");
        die();
    }
?>
</html>
<?php
    ob_flush();
?>

