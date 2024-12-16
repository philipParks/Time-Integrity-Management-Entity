package com.TIME.controller;

import com.TIME.dao.AppointmentQuery;
import com.TIME.dao.UserQuery;
import com.TIME.model.Appointment;
import com.TIME.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/** The log-in view controller. */
public class LoginController implements Initializable {
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label passwordLbl;
    @FXML
    private Label usernameLbl;
    @FXML
    private Label credentialsLbl;
    @FXML
    private Label timeTLbl;
    @FXML
    private Label timeILbl;
    @FXML
    private Label timeMLbl;
    @FXML
    private Label timeELbl;
    @FXML
    private Label zoneLabel;
    @FXML
    private Label warnLabel;
    @FXML
    private TextField passwordTxtFld;
    @FXML
    private TextField userNameTxtFld;

    public static User authUser;

    private ResourceBundle rb;

    /** Overrides the initialize method. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            rb = ResourceBundle.getBundle("com/TIME/Nat", Locale.getDefault());
        } catch (Exception e) {
            System.out.println(rb.getString("languageBundleError") + ": " + e.getMessage());
        }

        ZoneId systemZone = ZoneId.systemDefault();
        String zoneId = systemZone.getId();
        zoneLabel.setText(zoneId);
        warnLabel.setText("");
        timeTLbl.setText(rb.getString("timeT"));
        timeILbl.setText(rb.getString("timeI"));
        timeMLbl.setText(rb.getString("timeM"));
        timeELbl.setText(rb.getString("timeE"));
        credentialsLbl.setText(rb.getString("credentials"));
        usernameLbl.setText(rb.getString("username"));
        passwordLbl.setText(rb.getString("password"));
        submitButton.setText(rb.getString("submit"));
        cancelButton.setText(rb.getString("cancel"));

    }

    /** Verifies credentials and loads the main view. */
    public void submitButtonClicked(ActionEvent buttonClicked) throws IOException {
        String userName = userNameTxtFld.getText();
        String password = passwordTxtFld.getText();
        authUser = UserQuery.select(userName, password);
        // Set up the login_activity.txt file
        String fileName = "login_activity.txt";
        ZonedDateTime currentLocalZDT = ZonedDateTime.now();
        FileWriter writeToFile = new FileWriter(fileName, true);
        PrintWriter targetFile = new PrintWriter(writeToFile);
        // Set up the 15-minute alert
        Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
        appointmentAlert.setTitle(rb.getString("apptAlertTitle"));

        if (userName.isEmpty()) {
            warnLabel.setText("*** " + rb.getString("usernameError") + " ***");
            return;
        }

        if (password.isEmpty()) {
            warnLabel.setText("*** " + rb.getString("passwordError") + " ***");
            return;
        }

        if (authUser == null) {
            targetFile.println("Failed log in: " + currentLocalZDT + " Username: " + userName + " Password: " + password);
            targetFile.close();
            warnLabel.setText("*** " + rb.getString("credentialsError") + " ***");
            return;
        } else {
            targetFile.println("Successful log in: " + currentLocalZDT + " Username: " + userName);
            targetFile.close();
        }

        ObservableList<Appointment> appointments = AppointmentQuery.contactAppointments(authUser.getUserId());

        for (Appointment a : appointments) {
            LocalDateTime startDT = a.getStart();
            LocalDateTime currentDT  = LocalDateTime.now();
            long interval = ChronoUnit.MINUTES.between(currentDT, startDT);

            if (interval > 0 && interval <= 15) {
                appointmentAlert.setHeaderText(rb.getString("thereIsAnAppointment"));
                appointmentAlert.setContentText(rb.getString("appointmentId") + ": " + a.getAppointmentId() + "\n" +
                        rb.getString("displayDate") + ": " + startDT.toLocalDate().toString() + "\n" +
                        rb.getString("timeT") + ": " + startDT.toLocalTime().toString());
                appointmentAlert.showAndWait();

                FXMLLoader mainViewLoader = new FXMLLoader();
                mainViewLoader.setLocation(getClass().getResource("/com/TIME/view/mainView.fxml"));
                mainViewLoader.load();

                Parent mainViewParent = mainViewLoader.getRoot();
                Scene mainViewScene = new Scene(mainViewParent);
                Stage mainViewWindow = (Stage) ((Node) buttonClicked.getSource()).getScene().getWindow();

                mainViewWindow.setTitle("T.I.M.E.");
                mainViewWindow.setScene(mainViewScene);
                mainViewWindow.show();
                return;
            }

        }

        appointmentAlert.setHeaderText(rb.getString("thereIsNotAnAppointment"));
        appointmentAlert.setContentText(rb.getString("noAppointment"));
        appointmentAlert.showAndWait();

        FXMLLoader mainViewLoader = new FXMLLoader();
        mainViewLoader.setLocation(getClass().getResource("/com/TIME/view/mainView.fxml"));
        mainViewLoader.load();

        Parent mainViewParent = mainViewLoader.getRoot();
        Scene mainViewScene = new Scene(mainViewParent);
        Stage mainViewWindow = (Stage) ((Node) buttonClicked.getSource()).getScene().getWindow();

        mainViewWindow.setTitle("T.I.M.E.");
        mainViewWindow.setScene(mainViewScene);
        mainViewWindow.show();
    }

    /** Closes the application. */
    public void cancelButtonClicked() {
        Alert exitAlert = new Alert(Alert.AlertType.WARNING, rb.getString("cancelAlertText"));
        exitAlert.setTitle(rb.getString("cancelAlertTitle"));
        exitAlert.setHeaderText(rb.getString("cancelAlertHeader"));
        Optional<ButtonType> result = exitAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

}
