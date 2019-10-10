package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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


    public Controller() {
//        Client client = new Client("localhost", 8085);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // the is the initializing method
    }

    @FXML
    private void handleClicks(ActionEvent event) {

        if (event.getSource() == btnNewCustomer) {
            // New Customer
            labelStatus.setText("Customers");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(113, 86, 221),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            pnAddCustomers.toFront();
        } else if (event.getSource() == btnNewVehicle) {
            // New Vehicle
            labelStatus.setText("Vehicles");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(43, 63, 99),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            pnAddVehicle.toFront();
        } else if (event.getSource() == btnRentVehicle) {
            // Rent Vehicle
            labelStatus.setText("Rentals");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(44, 99, 63),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            pnRentVehicle.toFront();
        } else if (event.getSource() == btnReturnVehicle) {
            // Return Vehicle
            labelStatus.setText("Returns");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(99, 43, 63),
                    CornerRadii.EMPTY, Insets.EMPTY)));
        } else if (event.getSource() == btnListAllRentals) {
            // List rentals
            labelStatus.setText("All Rentals");
            status.setBackground(new Background(new BackgroundFill(Color.rgb(42, 28, 66),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            pnAllRentals.toFront();
        }

    }

}
