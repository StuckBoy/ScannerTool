# ScannerTool
Final Project for CS480

This project is to server as my "Magnum Opus" for my Mobile Web and App Development bachelor's degree from NMU.

The core concept is based around package tracking, a la USPS, FedEx, UPS, etc. With an android device, users can either login as a certified scanner to scan and track packages, or just lookup packages using a given tracking number.

The backend is planned to be based in mysql, as the need for scaleability of transactions is important. The interface will revolve around polling the mysql database, and a companion website using the same functionality will be constructed as well.

The hope is for the end project to have the ability to scan packages, upload data regarding the package to a mysql database, and for users to look up not only the last scanned location of the package, but also see the tracked history for a package. (Still working out the whole tracking multiple location histories...)

Happy path also includes a duplicate app for iPhones, as most companies that supply company phones tend to prefer Apple products. 
