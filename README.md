# ScannerTool
Final Project for CS480

This project is to serve as my "Magnum Opus" for my Mobile Web and App Development Bachelor's Degree from Northern Michigan University.

The core concept is based around package tracking, a la USPS, FedEx, UPS, etc. With an Android device, users can either login as a certified scanner to scan and track packages, or just lookup packages using a given tracking number.

The backend is planned to be based in mysql, as the need for scaleability of transactions is important. The interface will revolve around polling the mysql database using PHP with data sent from either the Android device or from a standalone website.

The hope is for the end project to have the ability to scan packages, upload data regarding the package to a mysql database, and for users to look up not only the last scanned location of the package, but also see the tracked history for a package. In addition, I want to be able to link coordinates to locations on a map such as linking to Google Maps for desktop, or the MapView library on Android. In addition, users will be able to create profiles that allow them to quickly look-up packages that they are interested in, and recieve push notifactions and/or e-mail upon updates reaching the database.

As a stretch goal, I'd also like to be able to create a iPhone equivalent application using the Swift language in XCode. While The main focus of the project is to provide a well-built Android and Web based application, having an iPhone variant would allow for greater coverage, as most phones supplied by businesses are typically iPhones.
