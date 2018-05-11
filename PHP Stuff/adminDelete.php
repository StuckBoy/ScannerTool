<?php
    require('adminCheck.php');
    ob_start();
?>

<html>
    <h1>Package Removal Page</h1>
    <form method="post" action="adminPage.php">
        <button type="submit" name="goBack">Go Back</button><br><br>
    </form>
</html>

<?php
    $pkgList = array();

    try {
        $dbh = new PDO('mysql:host=localhost;dbname=scannerTool;', 'zstuck', '');
        $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
        $activeList = $dbh->prepare("SELECT id, handler FROM activepkgs");
        $activeList->execute();
        echo '<form method="post" action="deletePkg.php">';
        while ($row = $activeList->fetch()) {
            echo '<span style="font-size: 18px;">';
            echo $row['id'] . ' Handler: ' . $row['handler'];
            echo ' <button type="submit" name=' . $row['id'] . '>Remove</button>';
            echo '</span><br>';
            $pkgList[] = $row['id'];
        }
        echo "</form>";
        $_SESSION['pkgList'] = $pkgList;
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
