<?php
    $currentPkgs = array();
    $pkgDetails = array();

    try {
        $dbh = new PDO('mysql:host=localhost;dbname=scannerTool', 'zstuck', '');
        $dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES,false);
        $currentStmt = $dbh->prepare("SELECT id from activepkgs");
        $currentStmt->execute();
        while ($row = $currentStmt->fetch()) {
            $currentPkgs[] = $row[0];
        }
    }
    catch (Exception $e) {
        print_r("Error!: " . $e->getMessage() . "<br>");
    }

    foreach ($_POST as $pkg => $pkgNum) {
        try {
            if (in_array($pkgNum, $currentPkgs)) {
                $stmt = $dbh->prepare("SELECT timestamp, coordinates, handler from `$pkgNum`");
                if (!$stmt) {
                    die();
                }
                else {
                    $stmt->execute();
                    $pkgDetails[] = $pkgNum;
                    while ($row = $stmt->fetch()) {
                        $pkgDetails[] = "_" . $row[0] . ", Coords: (" . $row[1] . "), Handler: " . $row[2];
                    }
                    $lastEntry = array_pop($pkgDetails);
                    $pkgDetails[] = $lastEntry . "_";
                }
            }
            else {
                echo "Error on " . $pkg . " -> " . $pkgNum;
            }
        }
        catch (Exception $e) {
            print_r("Error!: " . $e->getMessage() . "<br>");
            echo "Error looking up history on " . $pkNum;
        }
    }

    foreach ($pkgDetails as $line) {
        echo $line;
    }
?>
