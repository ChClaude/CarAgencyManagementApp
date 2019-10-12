package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project_classes.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private Button btnNewCustomer;

    @FXML
    private Button btnNewVehicle;

    @FXML
    private Button btnRentVehicle;

    @FXML
    private Button btnReturnVehicle;

    @FXML
    private Button btnListAllRentals;

    @FXML
    private Pane status;

    @FXML
    private Label labelStatus;

    @FXML
    private GridPane pnAddCustomers;

    @FXML
    private GridPane pnAllRentals;

    @FXML
    private GridPane pnAddVehicle;

    @FXML
    private GridPane pnRentVehicle;

    @FXML
    private GridPane pnReturnVehicle;

    @FXML
    private Button addCustomerBtn;

    // Add Customer Stage variable
    @FXML
    private Button saveNewCustomer;

    @FXML
    private Button closeAddCustomerStage;

    @FXML
    private Button closeIcon;

    @FXML
    private TextField customerIDNewCustomer;

    @FXML
    private TextField phoneNumberNewCustomer;

    @FXML
    private TextField firsNameNewCustomer;

    @FXML
    private TextField surnameNewCustomer;

    @FXML
    private CheckBox canRentNewCustomer;

    private Client client;

    /*public Controller() {

    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // this is the initializing method
        System.out.println("this is the initialize block");
        client = new Client("localhost", 8085);
    }

    @FXML
    private void handleClicks(ActionEvent event) {

        if (event.getSource() == btnNewCustomer) {
            // New Customer
            labelStatus.setText("Customers");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(113, 86, 221),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            managePaneVisibility(pnAddCustomers);
        } else if (event.getSource() == btnNewVehicle) {
            // New Vehicle
            labelStatus.setText("Vehicles");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(43, 63, 99),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            managePaneVisibility(pnAddVehicle);
        } else if (event.getSource() == btnRentVehicle) {
            /*
            * ****************
            *   RENT VEHICLE *
            * ****************
            * */
            labelStatus.setText("Rentals");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(44, 99, 63),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            managePaneVisibility(pnRentVehicle);
        } else if (event.getSource() == btnReturnVehicle) {
            // Return Vehicle
            labelStatus.setText("Returns");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(99, 43, 63),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            managePaneVisibility(pnReturnVehicle);
        } else if (event.getSource() == btnListAllRentals) {
            // List rentals
            labelStatus.setText("All Rentals");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(42, 28, 66),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            managePaneVisibility(pnAllRentals);
        }

    }

    @FXML
    void handleAddCustomer(ActionEvent event) {
        Stage addCustomerStage = new Stage();
        Parent root = null;
        if (event.getSource() == addCustomerBtn) {
            try {
                root = FXMLLoader.load(getClass().getResource("layouts/add_customer_activity.fxml"));
                addCustomerStage.initStyle(StageStyle.UNDECORATED);
                addCustomerStage.setScene(new Scene(root));
                addCustomerStage.initModality(Modality.APPLICATION_MODAL);
                addCustomerStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == saveNewCustomer) {
            /*
             * *****************
             *   SAVE CUSTOMER *
             * *****************
             * */

            String custID = customerIDNewCustomer.getText();
            String phoneNumber = phoneNumberNewCustomer.getText();
            String firstName = firsNameNewCustomer.getText();
            String surname = surnameNewCustomer.getText();
            boolean canRent = canRentNewCustomer.isSelected();

            customerIDNewCustomer.setText("");
            phoneNumberNewCustomer.setText("");
            firsNameNewCustomer.setText("");
            surnameNewCustomer.setText("");
            canRentNewCustomer.setText("");

            Customer customer = new Customer(firstName, surname, custID, phoneNumber, canRent);
            String response = client.writeCustomerToServer(customer);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(response);

            alert.showAndWait();
        } else if (event.getSource() == closeAddCustomerStage) {
            ((Node) event.getSource()).getScene().getWindow().hide();
        } else if (event.getSource() == closeIcon) {
            ((Node) event.getSource()).getScene().getWindow().hide();
        }
    }

    private void managePaneVisibility(Pane pane) {
        Pane[] panes = {pnAddCustomers, pnAllRentals, pnAddVehicle, pnRentVehicle, pnReturnVehicle};
        for (Pane cpane : panes) {
            if (pane.equals(cpane)) {
                cpane.setVisible(true);
            } else {
                cpane.setVisible(false);
            }
        }
    }

}
