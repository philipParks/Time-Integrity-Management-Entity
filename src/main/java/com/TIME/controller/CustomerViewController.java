package com.TIME.controller;

import com.TIME.dao.CountriesQuery;
import com.TIME.dao.CustomerQuery;
import com.TIME.dao.FirstLevelDivisionsQuery;
import com.TIME.helper.FullNameInterface;
import com.TIME.model.Country;
import com.TIME.model.Customer;
import com.TIME.model.FirstLevelDivision;
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
import java.util.Optional;
import java.util.ResourceBundle;

/** The customer view controller. */
public class CustomerViewController implements Initializable {
    @FXML
    private Label idLabel;
    @FXML
    private TextField firstNameTf;
    @FXML
    private TextField lastNameTf;
    @FXML
    private TextField addressTf;
    @FXML
    private TextField postalCodeTf;
    @FXML
    private TextField phoneTf;
    @FXML
    private Label systemMessage;
    @FXML
    private ComboBox<FirstLevelDivision> divisionCb;
    @FXML
    private ComboBox<Country> countryCb;

    private final ObservableList<Country> countries = Country.getCountries();

    /** Lambda 2: Converts a first name and a last name into a full name. */
    FullNameInterface name = (firstName, lastName) -> firstName + " " + lastName;

    /** Gets initial data for the view.
     * @param selectedCustomer The customer object selected for updating. */
    public void importCustomer(Customer selectedCustomer) {
        idLabel.setVisible(true);
        idLabel.setText(String.valueOf(selectedCustomer.getCustomerId()));

        int spaceIndex = selectedCustomer.getName().lastIndexOf(' ');

        if (spaceIndex == -1) {
            firstNameTf.setText(selectedCustomer.getName());
        } else {
            firstNameTf.setText(selectedCustomer.getName().substring(0, spaceIndex));
            lastNameTf.setText(selectedCustomer.getName().substring(spaceIndex + 1));
        }

        Country country = CountriesQuery.select(selectedCustomer.getDivision().getCountryId());
        countryCb.setValue(country);
        ObservableList<FirstLevelDivision> divByCountry = FirstLevelDivisionsQuery.select(countryCb.getValue().getCountryId());
        divisionCb.setItems(divByCountry);
        divisionCb.setValue(selectedCustomer.getDivision());
        addressTf.setText(selectedCustomer.getStreetAddress());
        postalCodeTf.setText(selectedCustomer.getPostalCode());
        phoneTf.setText(selectedCustomer.getPhone());
    }

    /** Overrides the initialize method with initial data for the view.
     * @param url The uniform resource locator associated with this controller.
     * @param resourceBundle The resource bundle associated with this controller. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        systemMessage.setText("");
        countryCb.setItems(countries);
        divisionCb.setPromptText("Select a Country");
    }

    /** Saves new customer data to the database and loads the main view.
     * <p><b>
     *     Lambda 2: Converts a first name and a last name into a full name.
     * </b></p>
     * @param saveButtonClicked The save button was clicked. */
    public void saveButtonClicked(ActionEvent saveButtonClicked) throws IOException {
        int id = Integer.parseInt(idLabel.getText());
        String firstName = firstNameTf.getText();
        String lastName = lastNameTf.getText();
        String address = addressTf.getText();
        String postalCode = postalCodeTf.getText();
        String phone = phoneTf.getText();
        int divisionId;

        if (firstName.isBlank() && lastName.isBlank()) {
            systemMessage.setText("*** Please enter a name into first name and/or last name ***");
            return;
        }
        if (address.isBlank()) {
            systemMessage.setText("*** Please enter an address ***");
            return;
        }
        if (postalCode.isBlank()) {
            systemMessage.setText("*** Please enter a postal code ***");
            return;
        }
        if (phone.isBlank()) {
            systemMessage.setText("*** Please enter a phone number ***");
            return;
        }
        if (divisionCb.getValue() == null) {
            systemMessage.setText("*** Please select a state/province ***");
            divisionId = 0;
        } else {
            divisionId = divisionCb.getValue().getDivisionId();
        }

        if (id > 0) {
            CustomerQuery.update(id, name.toFullName(firstName, lastName), address, postalCode, phone, divisionId);
        } else {
            CustomerQuery.insert(name.toFullName(firstName, lastName), address, postalCode, phone, divisionId);
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

    /** Confirms cancel and loads the main view.
     * @param cancelButtonClicked The cancel button was clicked. */
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

    /** Sets the options in the divisions comboBox. */
    public void countrySelected() {
        divisionCb.setPromptText("select a State/Province");
        ObservableList<FirstLevelDivision> divByCountry = FirstLevelDivisionsQuery.select(countryCb.getValue().getCountryId());
        divisionCb.setItems(divByCountry);
    }
}
