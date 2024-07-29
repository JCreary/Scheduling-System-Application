package com.example.c195project.Controller;

import com.example.c195project.Model.DBAppointments;
import com.example.c195project.Model.DBUsers;
import com.example.c195project.Model.Users;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * LoginMenuController class allows for the user to log in to the scheduling system.
 *
 * @author Jamal Creary
 */

public class LoginMenuController {

    public TextField userNameLoginMenu;
    public TextField passwordLoginMenu;
    public TextField timeZoneLoginMenu;
    public Text headerTextLoginMenu;
    public Text userNameTextLoginMenu;
    public Text passwordTextLoginMenu;
    public Button loginTextLoginMenu;
    public Button exitTextLoginMenu;
    public Text timeZoneTextLoginMenu;

    /**
     * Initializes the login menu
     *
     * @throws SQLException if there is an error accessing the SQL database
     */

    public void initialize() throws SQLException {
        // Sets the default time zone
        timeZoneLoginMenu.setText(ZoneId.systemDefault().toString());

        try {
            // Sets text values
            Locale.setDefault(Locale.getDefault());
            ResourceBundle rb = ResourceBundle.getBundle("Nat_loginmenu");
            headerTextLoginMenu.setText(rb.getString("LoginMenu"));
            userNameTextLoginMenu.setText(rb.getString("UserName"));
            passwordTextLoginMenu.setText(rb.getString("Password"));
            loginTextLoginMenu.setText(rb.getString("Login"));
            exitTextLoginMenu.setText(rb.getString("Exit"));
            timeZoneTextLoginMenu.setText(rb.getString("TimeZone"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * On click, it handles the login action event.
     * LambdaExpression#1: lambda expressions are passed as arguments to the
     * logLoginAttempt method. The lambda expression provides a concise way
     * to define implementations of functional interfaces.
     *
     * @param actionEvent
     * @throws IOException from FXMLLoader
     */
    public void onClickLogin(ActionEvent actionEvent) throws IOException {
        // Retrieve the username and password entered by the user
        String username = userNameLoginMenu.getText();
        String password = passwordLoginMenu.getText();

        // Load the appropriate resource bundle based on the system's language setting
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("Nat_loginmenu", locale);

        // Query the database to retrieve the user with the entered username
        try {
            ObservableList<Users> users = DBUsers.getAllUsers();
            boolean userAuthentication = false;
            // Iterates over the list of users to authenticate
            for (Users user : users) {
                // Check if the username exists in the database
                if (user.getUserName().equals(username)) {
                    // If the username exists, check if the password matches
                    if (user.getPassword().equals(password)) {
                        userAuthentication = true;
                        // Exits the loop if authentication is successful
                        break;
                    }
                }
            }
            // If authentication is successful, proceed to the main menu
            if (userAuthentication) {
                // Lambda expression#1 displays message for successful login attempt
                // If authentication is successful, log the login attempt with a lambda expression for logging
                UserActivityLogger.logLoginAttempt(username, true, message -> System.out.println(message));
                // Call the method to check upcoming appointments
                new DBAppointments().checkUpcomingAppointments();
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent scene = FXMLLoader.load(getClass().getResource("/com/example/c195project/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                locale = Locale.getDefault();
                rb = ResourceBundle.getBundle("Nat_loginmenu", locale);
                // Lambda expression#1 displays message for failed login attempt
                // If authentication fails, log the login attempt with a lambda expression for logging
                UserActivityLogger.logLoginAttempt(username, false, message -> System.err.println(message));
                // If authentication fails, display an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("AuthenticationFailed"));
                alert.setHeaderText(rb.getString("InvalidUsernameOrPassword"));
                alert.setContentText(rb.getString("PleaseEnterAValidUsernameAndPassword."));
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
            locale = Locale.getDefault();
            rb = ResourceBundle.getBundle("Nat_loginmenu", locale);
            // Display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("DatabaseError"));
            alert.setHeaderText(rb.getString("ErrorAccessingDatabase"));
            alert.setContentText(rb.getString("AnErrorOccurredWhileAccessingTheDatabase.PleaseTryAgainLater."));
            alert.showAndWait();
        }
    }

    /**
     * Handles the exit button. Upon click the user exits the system.
     *
     * @param actionEvent
     */
    public void onClickExit(ActionEvent actionEvent) {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("Nat_loginmenu", locale);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.getString("ConfirmThatYouWouldLikeToExitProgram"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * The UserActivityLogger stores the user login attempts in the txt file.
     */
    public class UserActivityLogger {
        //Shows the log file path
        private static final String LOG_FILE_PATH = "login_activity.txt";
        /**
         * Logs a user login attempt to the txt file.
         *
         * @param usernameEntered  username entered by the user
         * @param successfulLogin  true only if login attempt is successful
         * @param logger  instance used for logging
         */
        public static void logLoginAttempt(String usernameEntered, boolean successfulLogin, Consumer<String> logger) {
            try (FileWriter fw = new FileWriter(LOG_FILE_PATH, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                LocalDateTime now = LocalDateTime.now();
                String status = successfulLogin ? "Success" : "Failure";
                //Displays the format for the logged entry
                String loggedEntry = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + " - User '" + usernameEntered + "' attempted login. Status: " + status;
                // Writes the logged entry to the log file
                pw.println(loggedEntry);
                //Passes the logged entry to the logger instance using a lambda expression
                logger.accept(loggedEntry);
                //Prints success message
                System.out.println("Login activity logged successfully.");
                //Catches errors.
            } catch (IOException e) {
                System.err.println("Error occurred while logging login activity: " + e.getMessage());
            }
        }
    }
}
