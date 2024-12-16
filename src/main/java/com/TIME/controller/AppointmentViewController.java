package com.TIME.controller;

import com.TIME.dao.*;
import com.TIME.helper.DateTimeInterface;
import com.TIME.model.*;
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

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** The appointment view controller. */
public class AppointmentViewController implements Initializable {
    @FXML
    private ComboBox<Customer> customerCb;
    @FXML
    private Label idLabel;
    @FXML
    private TextField titleTf;
    @FXML
    private TextField descriptionTf;
    @FXML
    private TextField locationTf;
    @FXML
    private ComboBox<Contact> contactCb;
    @FXML
    private TextField typeTf;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<LocalTime> startCb;
    @FXML
    private ComboBox<LocalTime> endCb;
    @FXML
    private ComboBox<User> userCb;
    @FXML
    private Label systemLabel;

    private final ObservableList<Contact> contacts = ContactsQuery.selectAll();
    private final ObservableList<Customer> customers = CustomerQuery.selectAll();
    private final ObservableList<User> users = UserQuery.selectAll();

    /** Lambda 1: Converts a time from EST to local time. */
    static DateTimeInterface estToLocal = (localDate, localTime) -> {
        ZoneId myZone = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId EST = ZoneId.of("America/New_York");
        ZonedDateTime estZDT = ZonedDateTime.of(localDate, localTime, EST);
        ZonedDateTime localZDT = estZDT.withZoneSameInstant(myZone);
        return localZDT.toLocalDateTime();
    };

    /** Gets initial data for the view.
     * @param selectedAppointment The appointment object selected for updating. */
    public void importAppointment(Appointment selectedAppointment) {
        idLabel.setVisible(true);
        idLabel.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        titleTf.setText(String.valueOf(selectedAppointment.getTitle()));
        descriptionTf.setText(String.valueOf(selectedAppointment.getDescription()));
        locationTf.setText(String.valueOf(selectedAppointment.getLocation()));
        contactCb.setValue(ContactsQuery.select(selectedAppointment.getContact().getContactId()));
        typeTf.setText(String.valueOf(selectedAppointment.getType()));
        datePicker.setValue(selectedAppointment.getStart().toLocalDate());
        startCb.setValue(selectedAppointment.getStart().toLocalTime());
        endCb.setValue(selectedAppointment.getEnd().toLocalTime());
        Customer customer = CustomerQuery.select(selectedAppointment.getCustomer());
        customerCb.setValue(customer);
        User user = UserQuery.select(selectedAppointment.getUser());
        userCb.setValue(user);
    }

    /** Overrides the initialize method of the AppointmentViewController.
     * <p><b>
     *  Lambda 1: Converts a time from EST to local time.
     * </b></p>
     * @param url The uniform resource locator.
     * @param resourceBundle The resource bundle. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate date = LocalDate.now();
        LocalTime estStartTime = LocalTime.of(8, 0);
        LocalTime estLastApptStart = LocalTime.of(21, 45);
        LocalTime estFirstApptEnd = LocalTime.of(8, 15);
        LocalTime estEndTime = LocalTime.of(22, 0);
        LocalDateTime localStart = estToLocal.convertToDateTime(date,estStartTime);
        LocalDateTime localLastStart = estToLocal.convertToDateTime(date, estLastApptStart);
        LocalDateTime localFirstEnd = estToLocal.convertToDateTime(date, estFirstApptEnd);
        LocalDateTime localEnd = estToLocal.convertToDateTime(date, estEndTime);

        systemLabel.setText("");
        contactCb.setItems(contacts);
        customerCb.setItems(customers);
        userCb.setItems(users);

        while (localStart.isBefore(localLastStart.plusSeconds(1))) {
            startCb.getItems().add(localStart.toLocalTime());
            localStart = localStart.plusMinutes(15);
        }
        while (localFirstEnd.isBefore(localEnd.plusSeconds(1))) {
            endCb.getItems().add(localFirstEnd.toLocalTime());
            localFirstEnd = localFirstEnd.plusMinutes(15);
        }

    }

    /** Saves appointment data to the client schedule database and loads the main view.
     * @param saveButtonClicked The save button is clicked. */
    public void saveButtonClicked(ActionEvent saveButtonClicked) throws IOException {
        int id = Integer.parseInt(idLabel.getText());
        String title = titleTf.getText();
        String description = descriptionTf.getText();
        String location = locationTf.getText();
        String type = typeTf.getText();
        LocalDate date = datePicker.getValue();
        LocalTime start = startCb.getValue();
        LocalDateTime newStartDateTime;
        LocalTime end = endCb.getValue();
        LocalDateTime newEndDateTime;
        Customer customer = customerCb.getValue();
        int customerId;
        Contact contact = contactCb.getValue();
        int contactId;
        User user = userCb.getValue();
        int userId;

        if (title.isBlank()) {
            systemLabel.setText("*** Please enter a title for the appointment ***");
            return;
        }
        if (description.isBlank()) {
            systemLabel.setText("*** Please enter a description for the appointment ***");
            return;
        }
        if (location.isBlank()) {
            systemLabel.setText("*** Please enter a location for the appointment ***");
            return;
        }
        if (type.isBlank()) {
            systemLabel.setText("*** Please enter an appointment type ***");
            return;
        }
        if (date == null) {
            systemLabel.setText("*** Please select a date ***");
            return;
        }

        if (start == null) {
            systemLabel.setText("*** Please select a start time ***");
            return;
        }
        newStartDateTime = LocalDateTime.of(date, start);

        if (end == null) {
            systemLabel.setText("*** Please select an end time ***");
            return;
        }
        newEndDateTime = LocalDateTime.of(date, end);

        if (end.isBefore(start) || end.equals(start)) {
            systemLabel.setText("*** The end time must be after the start time ***");
        }

        if (customer == null) {
            systemLabel.setText("*** Please select a customer ***");
            return;
        }
        customerId = customer.getCustomerId();
        ObservableList<Appointment> customerAppointments = AppointmentQuery.contactAppointments(customerId);

        if (contact == null) {
            systemLabel.setText("*** Please select a contact ***");
            return;
        }
        contactId = contact.getContactId();

        if (user == null) {
            systemLabel.setText("*** Please select a user ***");
            return;
        }
        userId = user.getUserId();

        for (Appointment a : customerAppointments) {
            LocalDateTime existingStartTime = a.getStart();
            LocalDateTime existingEndTime = a.getEnd();
            int appointmentId = a.getAppointmentId();

            if (appointmentId == id && existingStartTime.equals(newStartDateTime) && existingEndTime.equals(newEndDateTime)) {
                break;
            }

            if (newStartDateTime.isAfter(existingStartTime) && newStartDateTime.isBefore(existingEndTime)) {
                systemLabel.setText("*** " + customer + " will be at Appointment ID: " + a.getAppointmentId()+ ". Please select another start time ***"); // appointment has not ended yet
                return;
            }

            if (newEndDateTime.isAfter(existingStartTime) && newEndDateTime.isBefore(existingEndTime)) {
                systemLabel.setText("*** " + customer + " has Appointment ID: " + a.getAppointmentId()+ " beginning before the end time. Please select another end time ***");
                return;
            }

            if ((newStartDateTime.isBefore(existingStartTime) || newStartDateTime.equals(existingStartTime)) && (newEndDateTime.equals(existingEndTime) || newEndDateTime.isAfter(existingEndTime))) {
                systemLabel.setText("*** " + customer + " has a conflicting appointment. Appointment ID: " + a.getAppointmentId()+ " ***");
                return;
            }

        }

        if (id > 0) {
            AppointmentQuery.update(id, title, description, location, type, newStartDateTime, newEndDateTime, userId, customerId, contactId);
        } else {
            AppointmentQuery.insert(title, description, location, type, newStartDateTime, newEndDateTime, userId, customerId, contactId);
        }

        FXMLLoader mainViewLoader = new FXMLLoader();
        mainViewLoader.setLocation(getClass().getResource("/com/TIME/view/mainView.fxml"));
        mainViewLoader.load();

        Parent mainViewParent = mainViewLoader.getRoot();
        Scene mainViewScene = new Scene(mainViewParent);
        Stage mainViewWindow = (Stage) ((Node)saveButtonClicked.getSource()).getScene().getWindow();

        mainViewWindow.setTitle("T.I.M.E.");
        mainViewWindow.setScene(mainViewScene);
        mainViewWindow.show();
    }

    /** Loads the main view.
     * @param cancelButtonClicked The cancel button is clicked. */
    public void cancelButtonClicked(ActionEvent cancelButtonClicked) throws IOException {
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("Cancel dialog");
        cancelAlert.setHeaderText("Are you sure you want to cancel?" + "\n");
        cancelAlert.setContentText("""
                Changes you have made may not be saved.
                
                Click "OK" to return to the main screen.
                Click "Cancel" to continue to edit this appointment.""");
        Optional<ButtonType> result = cancelAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader mainViewLoader = new FXMLLoader();
            mainViewLoader.setLocation(getClass().getResource("/com/TIME/view/mainView.fxml"));
            mainViewLoader.load();

            Parent mainViewParent = mainViewLoader.getRoot();
            Scene mainViewScene = new Scene(mainViewParent);
            Stage mainViewWindow = (Stage) ((Node)cancelButtonClicked.getSource()).getScene().getWindow();

            mainViewWindow.setTitle("T.I.M.E.");
            mainViewWindow.setScene(mainViewScene);
            mainViewWindow.show();
        }

    }

}
