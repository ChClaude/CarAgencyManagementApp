package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import sample.Controller;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerController extends Controller {

    @FXML
    private Button saveNewCustomer;

    @FXML
    private Button closeAddCustomerStage;

    @FXML
    private TextField customerIDNewCustomer;

    @FXML
    private TextField phoneNumberNewCustomer;

    @FXML
    private TextField surnameNewCustomer;

    @FXML
    private TextField firsNameNewCustomer;

    @FXML
    private CheckBox canRentNewCustomer;

    @FXML
    private Button closeIcon;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize AddCustomerStage
    }

    @FXML
    void handleAddCustomerActivities(ActionEvent event) {
        super.handleAddCustomer(event);
    }

}
