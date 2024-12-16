package com.TIME;

import com.TIME.dao.JDBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** Contains the 'main' method for the TIME application. */
public class TIMEMain extends Application {

    /** Overrides the start method with the loginView.
     * @param window Login window. */
    @Override
    public void start(Stage window) throws IOException {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle("com/TIME/Nat", Locale.getDefault());
        } catch (Exception e) {
            System.out.println("Something went wrong with the language pack: " + e.getMessage());
        }
        FXMLLoader loginViewLoader = new FXMLLoader(TIMEMain.class.getResource("view/loginView.fxml"));
        Scene loginScene = new Scene(loginViewLoader.load(), 600, 400);
        window.setTitle("T.I.M.E. " + rb.getString("login"));
        window.setScene(loginScene);
        window.show();
    }

    /** The main method for the TIME application.
     * @param args Application arguments. */
    public static void main(String[] args) {
        JDBConnection.openConnection();
        launch();
        JDBConnection.closeConnection();
    }
}