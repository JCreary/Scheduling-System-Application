package com.example.c195project.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * This class created to manage the database connection for the application.
 *
 * @author Jamal Creary
 */

public abstract class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password

    public static Connection connection;  // Connection Interface

    /**
     * Opens a connection to the database.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(SQLException e)
        {
            System.out.println("Error:" + e.getMessage());
        }
        //Use printstacktrace for outputting exceptions
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the connection to the database. Needed to get the connection
     *
     * @return The connection to the database.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection to the database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

}
