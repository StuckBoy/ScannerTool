<?php
    require('adminCheck.php');
    ob_start();
?>

<html>
    <h1>Scanner Admin Page</h1>
    <h2>Please remember to log out before closing.</h2>
    <form method="post" action="logout.php">
        <button type="submit" name="deletePackage" formaction="adminDelete.php">Delete Package</button>
        <button type="submit" name="assignOwner" formaction="adminOwner.php">Update Tracking Info</button>
        <button type="submit" name="logout">Logout</button>
    </form>
</html>

<?php
    ob_flush();
?>
