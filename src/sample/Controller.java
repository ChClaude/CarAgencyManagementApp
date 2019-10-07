package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class Controller {

    public Controller() {
//        Client client = new Client("localhost", 8085);


   }

   @FXML
   public void addNewCustomer(ActionEvent e) {
       System.out.println("Add a new customer");
   }

    @FXML
    public void addNewVehicle(ActionEvent e) {
        System.out.println("Add a new vehicle");
    }

    @FXML
    public void rentVehicle(ActionEvent e) {
        System.out.println("rent a vehicle");
    }

    @FXML
    public void returnVehicle(ActionEvent e) {
        System.out.println("return a vehicle");
    }

    @FXML
    public void listAllVehicles(ActionEvent e) {
        System.out.println("list all vehicles");
    }

    @FXML
    public void listAllCustomers(ActionEvent e) {
        System.out.println("list all customers");
    }

    @FXML
    public void displayAllRentals(ActionEvent e) {
        System.out.println("Display all rentals");
    }

}
