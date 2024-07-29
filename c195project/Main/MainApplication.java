package com.example.c195project.Main;

import com.example.c195project.Helper.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class created to open and close the connection when the application is launched.
 *
 * @author Jamal Creary
 */


public class MainApplication extends Application {

    /**
     * Starts the application via the login menu controller.
     *
     * @param stage
     * @throws IOException if an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/c195project/LoginMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Scheduling System");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * This method opens the connection to the database when the application is launched,
     * then the connection is closed when the application is closed.
     */
    public static void main(String[] args) {
        DBConnection.openConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}