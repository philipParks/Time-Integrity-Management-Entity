package com.TIME.controller;

import com.TIME.dao.AppointmentQuery;
import com.TIME.dao.ContactsQuery;
import com.TIME.dao.CustomerQuery;
import com.TIME.helper.DateTimeInterface;
import com.TIME.model.Appointment;
import com.TIME.model.Contact;
import com.TIME.model.Customer;
import com.TIME.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** This is the main view controller. */
public class MainViewController implements Initializable {

    // PRIMARY MAIN VIEW CONTROLS
    @FXML
    private Tab customersTab;
    @FXML
    private Tab reportsTab;
    @FXML
    private Tab appointmentsTab;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button appointmentButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label warnLabel;

    // FOR THE REPORTS TAB AREA
    @FXML
    private TableView<Appointment> reportsTable;
    @FXML
    private TableColumn<?, Integer> scheduleIdCol;
    @FXML
    private TableColumn<?, String> scheduleTitleCol;
    @FXML
    private TableColumn<?, String> scheduleTypeCol;
    @FXML
    private TableColumn<?, String> scheduleDesCol;
    @FXML
    private TableColumn<?, LocalDateTime> scheduleStartCol;
    @FXML
    private TableColumn<?, LocalDateTime> scheduleEndCol;
    @FXML
    private TableColumn<?, Customer> scheduleCustomerCol;
    @FXML
    private ComboBox<Contact> contactsForScheduleCb;
    @FXML
    private ComboBox<String> appointmentTypeCb;
    @FXML
    private ComboBox<Month> monthCb;
    @FXML
    private ComboBox<String> locationCb;
    @FXML
    private Label TypeAndMonthTotalAppointments;
    @FXML
    private Label locationAppointmentTotal;

    // FOR THE APPOINTMENT TAB AREA
    @FXML
    private ToggleGroup appointmentViews;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<?, Integer> apptIdCol;
    @FXML
    private TableColumn<?, String> apptTitleCol;
    @FXML
    private TableColumn<?, String> apptDesCol;
    @FXML
    private TableColumn<?, String> apptLocCol;
    @FXML
    private TableColumn<?, String> apptTypeCol;
    @FXML
    private TableColumn<?, LocalDateTime> apptStartCol;
    @FXML
    private TableColumn<?, LocalDateTime> apptEndCol;
    @FXML
    private TableColumn<?, String> apptCustomerCol;
    @FXML
    private TableColumn<?, String> apptUserCol;
    @FXML
    private TableColumn<?, String> apptContactCol;

    // FOR THE CUSTOMER TAB AREA
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<?, Integer> customerIdCol;
    @FXML
    private TableColumn<?, String> customerNameCol;
    @FXML
    private TableColumn<?, String> streetAddressCol;
    @FXML
    private TableColumn<?, FirstLevelDivision> stateProvinceCol;
    @FXML
    private TableColumn<?, String> postalCodeCol;
    @FXML
    private TableColumn<?, String> phoneCol;

    // FOR THE QUICK VIEW AREA
    @FXML
    private Label greetingLabel;
    @FXML
    private Label quickViewLabel;
    @FXML
    private Label titleNameLabel;
    @FXML
    private Label descAddLabel;
    @FXML
    private Label locationStateLabel;
    @FXML
    private Label contactPostalLabel;
    @FXML
    private Label typePhoneLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label startLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label customerLabel;

    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    /** Lambda 1: Converts a time from EST to local time. */
    static DateTimeInterface estToLocal = (localDate, localTime) -> {
        ZoneId myZone = ZoneId.of(TimeZone.getDefault().getID());
        ZoneId EST = ZoneId.of("America/New_York");
        ZonedDateTime currentZDT = ZonedDateTime.of(localDate, localTime, myZone);
        ZonedDateTime estZDT = currentZDT.withZoneSameInstant(EST);
        return estZDT.toLocalDateTime();
    };

    /** Overrides the initialize method of the MainViewController.
     * @param url The uniform resource locator.
     * @param resourceBundle The resource bundle. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        greetingLabel.setText("Quick View: ");
        warnLabel.setText("");
        quickViewLabel.setText("Appointments Table");
        titleNameLabel.setText("");
        descAddLabel.setText("");
        locationStateLabel.setText("");
        contactPostalLabel.setText("");
        typePhoneLabel.setText("");
        dateLabel.setText("");
        startLabel.setText("");
        toLabel.setText("");
        endLabel.setText("");
        customerLabel.setText("");
        addCustomerButton.setVisible(false);
        appointmentButton.setVisible(true);
        modifyButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    // APPOINTMENT TAB CONTROLS
    /** Sets values and events for interface controls when the appointments tab is selected. */
    public void appointmentsTabSelected() {
        appointments = AppointmentQuery.selectAll();

        try {
            warnLabel.setText("");
            quickViewLabel.setText("Appointments Table");
            titleNameLabel.setText("");
            descAddLabel.setText("");
            locationStateLabel.setText("");
            contactPostalLabel.setText("");
            typePhoneLabel.setText("");
            dateLabel.setText("");
            startLabel.setText("");
            toLabel.setText("");
            endLabel.setText("");
            customerLabel.setText("");
            appointmentButton.setVisible(true);
            addCustomerButton.setVisible(false);
            modifyButton.setVisible(false);
            deleteButton.setVisible(false);
        } catch (Exception e) {
            // ignore
        }

        appointmentTable.setItems(appointments);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDesCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptLocCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        apptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("User"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));

    }

    /** Changes the appointment table view to show appointments in the next 7 days.
     *  This is the default setting. */
    public void weekSelected() {
        appointments = AppointmentQuery.selectAll();
        ObservableList<Appointment> oneWeekAppointments = FXCollections.observableArrayList();

        for (Appointment a : appointments) {

            if (!a.getStart().getMonth().equals(LocalDateTime.now().getMonth())) {
                return;
            } else if (a.getStart().isBefore(LocalDateTime.now().plusDays(7)) && !a.getStart().isBefore(LocalDateTime.now())) {
                oneWeekAppointments.add(a);
            }

        }

        appointmentTable.setItems(oneWeekAppointments);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDesCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptLocCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        apptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("User"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));

    }

    /** Changes the appointment table view to show appointments in the next 30 days. */
    public void monthSelected() {
        appointments = AppointmentQuery.selectAll();
        ObservableList<Appointment> oneMonthAppointments = FXCollections.observableArrayList();

        for (Appointment a : appointments) {
            Month appointmentMonth = a.getStart().getMonth();
            Month currentMonth = LocalDateTime.now().getMonth();

            if (appointmentMonth.equals(currentMonth)) {
                oneMonthAppointments.add(a);
            }

        }

        appointmentTable.setItems(oneMonthAppointments);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDesCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptLocCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        apptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("User"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));
    }

    /** Changes the appointment table view to show all appointments. */
    public void allSelected() {
        appointments = AppointmentQuery.selectAll();

        appointmentTable.setItems(appointments);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDesCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptLocCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("End"));
        apptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("User"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("Contact"));
    }

    /** Displays selected appointment details in the quick view area.
     * <p><b>
     *     Lambda 1: Converts a time from EST to local time.
     * </b></p>*/
    public void appointmentDetailsButtonClicked() {

        if (appointmentTable.getSelectionModel().isEmpty()) {
            warnLabel.setText("*** Please select an appointment by clicking on it in the table ***");
        }

        String title = appointmentTable.getSelectionModel().getSelectedItem().getTitle();
        String description = appointmentTable.getSelectionModel().getSelectedItem().getDescription();
        String location = appointmentTable.getSelectionModel().getSelectedItem().getLocation();
        int contactId = appointmentTable.getSelectionModel().getSelectedItem().getContact().getContactId();
        String type = appointmentTable.getSelectionModel().getSelectedItem().getType();
        LocalDateTime startDateTime = LocalDateTime.parse(appointmentTable.getSelectionModel().getSelectedItem().getStart().toString());
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDateTime endDateTime = LocalDateTime.parse(appointmentTable.getSelectionModel().getSelectedItem().getEnd().toString());
        LocalTime endTime = endDateTime.toLocalTime();
        int customerId = appointmentTable.getSelectionModel().getSelectedItem().getCustomer();
        Customer customer = CustomerQuery.select(customerId);
        LocalDateTime localStart = estToLocal.convertToDateTime(date, startTime);
        LocalDateTime localEnd = estToLocal.convertToDateTime(date, endTime);
        date = localStart.toLocalDate();
        startTime = localStart.toLocalTime();
        endTime = localEnd.toLocalTime();

        titleNameLabel.setText(title);
        descAddLabel.setText(description);
        locationStateLabel.setText(location);
        contactPostalLabel.setText(ContactsQuery.select(contactId).toString());
        typePhoneLabel.setText(type);
        dateLabel.setText(date.format(dateFormat));
        startLabel.setText(startTime.format(timeFormat));
        toLabel.setText("to");
        endLabel.setText(endTime.format(timeFormat));
        customerLabel.setText(customer.toString());
        modifyButton.setVisible(true);
        deleteButton.setVisible(true);
    }

    // REPORT TAB CONTROLS
    /** Sets values and events for interface controls when the reports tab is selected. */
    public void ReportsTabSelected() {
        warnLabel.setText("");
        TypeAndMonthTotalAppointments.setText("");
        locationAppointmentTotal.setText("");
        addCustomerButton.setVisible(false);
        appointmentButton.setVisible(false);
        modifyButton.setVisible(false);
        deleteButton.setVisible(false);
        // SETS COMBO BOXES FOR THE REPORTS AREA
        ObservableList<Contact> contacts = ContactsQuery.selectAll();

        ObservableList<String> allLocations = AppointmentQuery.getLocations();
        ObservableList<String> uniqueLocations = FXCollections.observableArrayList();

        for (String location : allLocations) {
            if (!uniqueLocations.contains(location)) {
                uniqueLocations.add(location);
            }
        }

        ObservableList<String> AllTypes = AppointmentQuery.getTypes();
        ObservableList<String> uniqueTypes = FXCollections.observableArrayList();

        for (String type : AllTypes) {
            if (!uniqueTypes.contains(type)) {
                uniqueTypes.add(type);
            }
        }

        contactsForScheduleCb.setItems(contacts);
        monthCb.getItems().setAll(Month.values());
        appointmentTypeCb.setItems(uniqueTypes);
        locationCb.setItems(uniqueLocations);


        // SETS THE QUICK VIEW AREA FOR REPORT INFORMATION
        quickViewLabel.setText("Report Table");
        titleNameLabel.setText("");
        descAddLabel.setText("");
        locationStateLabel.setText("");
        contactPostalLabel.setText("");
        typePhoneLabel.setText("");
        dateLabel.setText("");
        startLabel.setText("");
        toLabel.setText("");
        endLabel.setText("");
        customerLabel.setText("");
    }

    /** Shows the total amount of appointments of the selected type and month. */
    public void totalButtonClicked() {

        String type = appointmentTypeCb.getSelectionModel().getSelectedItem();
        Month month = monthCb.getSelectionModel().getSelectedItem();
        if (type == null) {
            warnLabel.setText("*** Please select an appointment Type ***");
            return;
        }

        if (month == null) {
            warnLabel.setText("*** Please select a month ***");
            return;
        }

        ObservableList<Appointment> appointmentsByType = AppointmentQuery.selectByType(type);
        ObservableList<Appointment> appointmentsByTypeAndMonth = FXCollections.observableArrayList();

        for (Appointment a : appointmentsByType) {
            System.out.println(a.getStart().getMonth());
            if (a.getStart().getMonth().equals(month)) {
                appointmentsByTypeAndMonth.add(a);
            }
        }

        if (appointmentsByTypeAndMonth.isEmpty()) {
            TypeAndMonthTotalAppointments.setText(String.valueOf(0));
        } else {
            TypeAndMonthTotalAppointments.setText(String.valueOf(appointmentsByTypeAndMonth.size()));
        }

        TypeAndMonthTotalAppointments.setVisible(true);

    }

    /** Populates the reports table with the selected contacts appointments. */
    public void populateReportTable() {

        try {
            int contactId = contactsForScheduleCb.getSelectionModel().getSelectedItem().getContactId();
            ObservableList<Appointment> contactAppointments = AppointmentQuery.contactAppointments(contactId);
            reportsTable.setItems(contactAppointments);
            scheduleIdCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentId"));
            scheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
            scheduleDesCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
            scheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
            scheduleStartCol.setCellValueFactory(new PropertyValueFactory<>("Start"));
            scheduleEndCol.setCellValueFactory(new PropertyValueFactory<>("End"));
            scheduleCustomerCol.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        } catch (Exception e) {
            warnLabel.setText("*** There are no appointments for the selected contact ***");
        }

    }

    /** Calculates the amount of appointments by location. 3rd report.*/
    public void locationSelected() {
        String location = locationCb.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> locationAppointments = AppointmentQuery.selectByLocation(location);

        locationAppointmentTotal.setText(String.valueOf(locationAppointments.size()));
    }

    // CUSTOMER TAB CONTROLS
    /** Sets values and events for interface controls when the customer tab is selected. */
    public void customerTabSelected() {
        warnLabel.setText("");
        quickViewLabel.setText("Customer Table");
        titleNameLabel.setText("");
        descAddLabel.setText("");
        locationStateLabel.setText("");
        contactPostalLabel.setText("");
        typePhoneLabel.setText("");
        dateLabel.setText("");
        startLabel.setText("");
        toLabel.setText("");
        endLabel.setText("");
        customerLabel.setText("");
        appointmentButton.setVisible(false);
        addCustomerButton.setVisible(true);
        modifyButton.setVisible(false);
        deleteButton.setVisible(false);

        // SETS UP THE CUSTOMER TABLE ON THE CUSTOMER TAB
        ObservableList<Customer> customers = CustomerQuery.selectAll();
        customerTable.setItems(customers);
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        streetAddressCol.setCellValueFactory(new PropertyValueFactory<>("StreetAddress"));
        stateProvinceCol.setCellValueFactory(new PropertyValueFactory<>("Division"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
    }

    /** Displays selected customer details in the quick view area. */
    public void CustomerDetailsButtonClicked() {

        if (customerTable.getSelectionModel().isEmpty()) {
            warnLabel.setText("*** Please select a customer by clicking it in the customer table ***");
            return;
        }

        titleNameLabel.setText(customerTable.getSelectionModel().getSelectedItem().getName());
        descAddLabel.setText(customerTable.getSelectionModel().getSelectedItem().getStreetAddress());
        locationStateLabel.setText(customerTable.getSelectionModel().getSelectedItem().getDivision().toString());
        contactPostalLabel.setText(customerTable.getSelectionModel().getSelectedItem().getPostalCode());
        typePhoneLabel.setText(customerTable.getSelectionModel().getSelectedItem().getPhone());
        dateLabel.setText("");
        startLabel.setText("");
        toLabel.setText("");
        endLabel.setText("");
        customerLabel.setText("");
        modifyButton.setVisible(true);
        deleteButton.setVisible(true);
    }

    // GENERAL INTERFACE CONTROLS
    /** Displays the appropriate modify window for the current selection.
     * @param actionEvent The modify button is clicked. */
    public void modifyButtonClicked(ActionEvent actionEvent) throws IOException {

        if (appointmentsTab.isSelected()) {
            FXMLLoader modifyAppointmentLoader = new FXMLLoader();
            modifyAppointmentLoader.setLocation(getClass().getResource("/com/TIME/view/appointmentView.fxml"));
            modifyAppointmentLoader.load();
            AppointmentViewController appointmentViewController = modifyAppointmentLoader.getController();

            appointmentViewController.importAppointment(appointmentTable.getSelectionModel().getSelectedItem());

            Parent modifyAppointmentParent = modifyAppointmentLoader.getRoot();
            Scene modifyAppointmentScene = new Scene(modifyAppointmentParent);
            Stage modifyAppointmentWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            modifyAppointmentWindow.setTitle("Modify Appointment");
            modifyAppointmentWindow.setScene(modifyAppointmentScene);
            modifyAppointmentWindow.show();
        }

        if (reportsTab.isSelected()) {
            FXMLLoader modifyAppointmentLoader = new FXMLLoader();
            modifyAppointmentLoader.setLocation(getClass().getResource("/com/TIME/view/appointmentView.fxml"));
            modifyAppointmentLoader.load();
            AppointmentViewController appointmentViewController = modifyAppointmentLoader.getController();

            appointmentViewController.importAppointment(reportsTable.getSelectionModel().getSelectedItem());

            Parent modifyAppointmentParent = modifyAppointmentLoader.getRoot();
            Scene modifyAppointmentScene = new Scene(modifyAppointmentParent);
            Stage modifyAppointmentWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            modifyAppointmentWindow.setTitle("Modify Appointment");
            modifyAppointmentWindow.setScene(modifyAppointmentScene);
            modifyAppointmentWindow.show();
        }

        if (customersTab.isSelected()) {
            FXMLLoader modifyCustomerLoader = new FXMLLoader();
            modifyCustomerLoader.setLocation(getClass().getResource("/com/TIME/view/customerView.fxml"));
            modifyCustomerLoader.load();
            CustomerViewController customerController = modifyCustomerLoader.getController();

            customerController.importCustomer(customerTable.getSelectionModel().getSelectedItem());

            Parent modifyCustomerParent = modifyCustomerLoader.getRoot();
            Scene modifyCustomerScene = new Scene(modifyCustomerParent);
            Stage modifyCustomerWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            modifyCustomerWindow.setTitle("Modify Customer Record");
            modifyCustomerWindow.setScene(modifyCustomerScene);
            modifyCustomerWindow.show();
        }

    }

    /** Deletes the selection based on which tab is active on the main view. */
    public void deleteButtonClicked() {

        if (appointmentsTab.isSelected()) {

            try {
                Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
                Alert deleteApptAlert = new Alert(Alert.AlertType.CONFIRMATION);
                deleteApptAlert.setTitle("Delete Appointment Dialog");
                deleteApptAlert.setHeaderText("Are you sure you want to delete the appointment titled " + appointment.getTitle() + "?");
                deleteApptAlert.setContentText("This action cannot be undone!");
                Optional<ButtonType> result = deleteApptAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    warnLabel.setText("*** Appointment ID: " + appointment.getAppointmentId() + " of Type: " + appointment.getType() + " has been canceled ***");
                    AppointmentQuery.delete(appointment.getAppointmentId());
                    appointmentTable.setItems(AppointmentQuery.selectAll());
                    quickViewLabel.setText("Appointments Table");
                    titleNameLabel.setText("");
                    descAddLabel.setText("");
                    locationStateLabel.setText("");
                    contactPostalLabel.setText("");
                    typePhoneLabel.setText("");
                    dateLabel.setText("");
                    startLabel.setText("");
                    toLabel.setText("");
                    endLabel.setText("");
                    customerLabel.setText("");
                    appointmentButton.setVisible(true);
                    addCustomerButton.setVisible(false);
                    modifyButton.setVisible(false);
                    deleteButton.setVisible(false);

                }

            } catch (Exception e) {
                warnLabel.setText("*** Please select an appointment to delete ***");
            }

        }

        if (customersTab.isSelected()) {
            try {
                Customer customer = customerTable.getSelectionModel().getSelectedItem();
                int customerId = customer.getCustomerId();
                ObservableList<Appointment> appointments = AppointmentQuery.contactAppointments(customerId);

                if (!appointments.isEmpty()) {
                    Alert associatedAppointments = new Alert(Alert.AlertType.WARNING);
                    associatedAppointments.setTitle("Associated Appointments Warning");
                    associatedAppointments.setHeaderText("There are " + appointments.size() + " Appointments " +
                            "still associated with \"" + customer + "\"");
                    associatedAppointments.setContentText("Please remove all Appointment associated with \"" + customer +
                            "\" by clicking on the \"Appointments Tab\" and removing the appointments with Customer ID: " + customerId);
                    Optional<ButtonType> result = associatedAppointments.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        return;
                    }

                }

                Alert deleteCustomerAlert = new Alert(Alert.AlertType.CONFIRMATION);
                deleteCustomerAlert.setTitle("Customer Delete Dialog");
                deleteCustomerAlert.setHeaderText("Are you sure you want to delete \"" + customer + "\"?");
                deleteCustomerAlert.setContentText("This action cannot be undone!");
                Optional<ButtonType> result = deleteCustomerAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    warnLabel.setText("*** " + customer + " has been deleted ***");
                    CustomerQuery.delete(customerId);
                    customerTable.setItems(CustomerQuery.selectAll());
                    quickViewLabel.setText("Customers Table");
                    titleNameLabel.setText("");
                    descAddLabel.setText("");
                    locationStateLabel.setText("");
                    contactPostalLabel.setText("");
                    typePhoneLabel.setText("");
                    dateLabel.setText("");
                    startLabel.setText("");
                    toLabel.setText("");
                    endLabel.setText("");
                    customerLabel.setText("");
                    appointmentButton.setVisible(false);
                    addCustomerButton.setVisible(true);
                    modifyButton.setVisible(false);
                    deleteButton.setVisible(false);
                }


            } catch (Exception e) {
                warnLabel.setText("*** Please select a customer to delete ***");
            }

        }

    }

    /** Displays the customer view with empty fields.
     * @param buttonClicked Add customer button clicked. */
    public void addCustomerButtonClicked(ActionEvent buttonClicked) throws IOException {
        FXMLLoader addCustomerViewLoader = new FXMLLoader();
        addCustomerViewLoader.setLocation(getClass().getResource("/com/TIME/view/customerView.fxml"));
        addCustomerViewLoader.load();

        Parent addCustomerViewParent = addCustomerViewLoader.getRoot();
        Scene addCustomerViewScene = new Scene(addCustomerViewParent);
        Stage addCustomerViewWindow = (Stage) ((Node)buttonClicked.getSource()).getScene().getWindow();

        addCustomerViewWindow.setTitle("Add Customer");
        addCustomerViewWindow.setScene(addCustomerViewScene);
        addCustomerViewWindow.show();
    }

    /** Displays the appointment view with empty fields.
     * @param buttonClicked Add appointment button clicked. */
    public void appointmentButtonClicked(ActionEvent buttonClicked) throws IOException {
        FXMLLoader scheduleAppointmentViewLoader = new FXMLLoader();
        scheduleAppointmentViewLoader.setLocation(getClass().getResource("/com/TIME/view/appointmentView.fxml"));
        scheduleAppointmentViewLoader.load();

        Parent scheduleAppointmentViewParent = scheduleAppointmentViewLoader.getRoot();
        Scene scheduleAppointmentViewScene = new Scene(scheduleAppointmentViewParent);
        Stage scheduleAppointmentViewWindow = (Stage) ((Node)buttonClicked.getSource()).getScene().getWindow();

        scheduleAppointmentViewWindow.setTitle("Add Appointment");
        scheduleAppointmentViewWindow.setScene(scheduleAppointmentViewScene);
        scheduleAppointmentViewWindow.show();
    }

    /** Return the user to the login screen.
     * @param buttonClicked Log-out button clicked. */
    public void logOutButtonClicked(ActionEvent buttonClicked)  throws IOException {
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Logout Confirmation Dialog");
        logoutAlert.setHeaderText("Are you sure you want to logout?");
        logoutAlert.setContentText("click \"OK\" to return to the login screen\nclick \"Cancel\" to stay logged in.");
        Optional<ButtonType> result = logoutAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loginViewLoader = new FXMLLoader();
            loginViewLoader.setLocation(getClass().getResource("/com/TIME/view/loginView.fxml"));
            loginViewLoader.load();

            Parent loginViewParent = loginViewLoader.getRoot();
            Scene loginViewScene = new Scene(loginViewParent);
            Stage loginViewWindow = (Stage) ((Node)buttonClicked.getSource()).getScene().getWindow();

            loginViewWindow.setTitle("T.I.M.E. Login");
            loginViewWindow.setScene(loginViewScene);
            loginViewWindow.show();
        }

    }

}
