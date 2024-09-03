# 30743581_khushaboo
## Overview:
Digital Market is a Java application designed to manage products, sellers, and transactions in an online marketplace. It leverages MySQL for data storage and provides a straightforward interface for various marketplace operations.


### Features:
-Product Management: Add, view, update, and delete products.
-Seller Management: Add, view, update, and delete sellers.
-Transaction Management: View, search, and delete transactions.

### Prerequisites:
-Java JDK 8 or higher
-MySQL Server
-MySQL Workbench

## Compilation

To compile the project, use the following command:

```bash
cd src
javac -d ../bin -cp ../lib/mysql-connector-java-8.0.26.jar app/*.java db/*.java manager/*.java

## Running the Application
```
To run the application, use the following command:

```bash
java -cp "../bin;../lib/mysql-connector-java-8.0.26.jar" app.DigitalMarketplaceApp
```
