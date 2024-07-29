# Scheduling-System-Application
Java Scheduling System Application

Title and Purpose of Application:
The title of the application is “Scheduling System.” The purpose of the application is to create a user-interactive scheduling appointment system with the incorporation of a database (via MySQL) to store customer records, contacts, appointment details, users, divisions, and countries. The user will need to log in with an authenticated username and password then they will be able to add, update, and delete customer records or appointments and view reports.

IDE Details:
IntelliJ IDEA 2023.3.2 (Community Edition)
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
JavaFX-SDK-17.0.6
Loads FXML document with JavaFX API of version 21 by JavaFX runtime of version 17.0.6

Directions on how to run the program:
Once the user runs the program, the user will be presented with the login screen. The login screen will be displayed in either French or English depending on the location of the user’s server. The time zone text field will list the location of the user’s system. The user will then be required to enter a valid username and password that matches a username and password combination in the user table of the MySQL database. Once the system determines that the username and password match a username and password in the database, the user login attempt will be logged in a txt file and the user will be transferred to the main menu screen. In the main menu screen, the user will have the ability to view reports and manipulate appointments and customer records. 

Description of the additional report:
For the additional report, I chose to create a report that displays customer IDs and the customer names of recently deleted customers. To create the report, I created an observable list in the DBCustomers class to store all deleted customers which is then called in the ReportsMenuController class. The reason I decided to create this report is because is to back up recently deleted customers. If the user makes a mistake and deletes a customer or is unsure which customer they just deleted they will be able to view the report and determine which customer was deleted. As long as the user does not log out of or close the program, they will be able to view deleted customers. 

MySQL Connector driver version number:
mysql-connector-java-8.0.31

