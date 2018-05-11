<?php
    $debug = 0;
    $currentPkgs = array();
    $pkgList = preg_split('/\r\n|[\r\n]/', $_POST["pkgNumbers"]);
    if ($debug) {
        echo '<pre>'; print_r($pkgList); echo '</pre>';
    }

    try {
        $dbh = new PDO('mysql:host=localhost;dbname=scannerTool', 'zstuck', '');
        $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
        $currentStmt = $dbh->prepare("SELECT id FROM activepkgs");
        $currentStmt->execute();
        while ($row = $currentStmt->fetch()) {
            $currentPkgs[] = $row[0];
        }
    }
    catch (Exception $e) {
        print_r("Error, " . $e->getMessage() . "<br>");
    }

    foreach ($pkgList as $pkg) {
        #Print the Bold Header
        echo '<div style="text-align: center;">';
        echo '<span style="font-size: 36px; text-decoration: underline">'.$pkg.'</span><br>';

        #Setup mysql stmt for every entry regarding pkg
        if (in_array($pkg, $currentPkgs)) {
            $lookupStmt = $dbh->prepare("SELECT timestamp, coordinates, handler from `$pkg`");
            if (!$lookupStmt) {
                die();
            }
            else {
                $lookupStmt->execute();
                while ($row = $lookupStmt->fetch()) {
                    #print the row
                    $noSpaceCoords = str_replace(' ', '', $row[1]);
                    echo '<span style="font-size: 16px;">';
                    echo $row['timestamp'] . ", Coords: (";
                    echo '<a href=http://www.google.com/maps/place/' . $noSpaceCoords . '>' . $row['coordinates'] .'</a>';
                    echo "), Handler: " . $row['handler'] . '</span><br>';
                    if ($debug) {
                        echo '<pre>'; print_r($row); echo '</pre>';
                    }
                }
            }
        }
        #close the table
        echo '</div>';
    }
?>
