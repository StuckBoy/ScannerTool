<?php
    session_start();
    if (!isset($_SESSION["admin"])) {
        header("Location: ./webView.php");
        exit();
    }
?>
