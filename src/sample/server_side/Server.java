package sample.server_side;

import project_classes.Customer;
import project_classes.Vehicle;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private Connection conn = null;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(15000);

        populateDatabase();
    }

    private void populateDatabase() {
        try {
            connectToDatabase();
            createTables();
            loadDataFromFiles();
        } catch (SQLException e) {
            System.err.println("An error occurred " + e);
        }

    }

    private void loadDataFromFiles() {
        try (ObjectInputStream customerInputStream = new ObjectInputStream(Files.newInputStream(
                Paths.get("../Customers.ser")));

             ObjectInputStream vehicleInputStream = new ObjectInputStream(Files.newInputStream(
                     Paths.get("../Vehicles.ser")));
             Statement statement = conn.createStatement()) {

            getDataFromCustomerSeqFileToDb(customerInputStream, statement);
            getDataFromVehicleSeqFileToDb(vehicleInputStream, statement);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Data loaded from files to database");
    }

    private void getDataFromCustomerSeqFileToDb(ObjectInputStream customerInputStream, Statement statement) throws IOException, SQLException {
        while (true) {
            int columns = 0;
            try {
                // loading customers to database
                Customer customer = (Customer) customerInputStream.readObject();

                insertCustomerToDb(statement, customer);

                columns++;
            } catch (EOFException ex) {
                System.out.println("End of file");
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found");
            } finally {
                System.out.println(columns + " affected in table CUSTOMERS.");
            }
        }
    }

    private void getDataFromVehicleSeqFileToDb(ObjectInputStream vehicleInputStream, Statement statement) throws IOException, SQLException {
        int columns = 0;
        while (true) {
            try {
                // loading vehicles to database
                Vehicle vehicle = (Vehicle) vehicleInputStream.readObject();
                insertVehicleToDb(statement, vehicle);

                columns++;
            } catch (EOFException ex) {
                System.out.println("End of file");
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found");
            } finally {
                System.out.println(columns + "affected in table VEHICLES.");
            }
        }
    }

    // this method inserts a customer object to the database
    private void insertCustomerToDb(Statement statement, Customer customer) throws SQLException {
        statement.executeUpdate("INSERT INTO CUSTOMERS(name, surname, idNum, phoneNum, canRent) " +
                "VALUES(" + customer.getName() + ", " + customer.getSurname() + ", " + customer.getIdNum()
                + ", " + customer.getPhoneNum() + ", " + customer.canRent() + ")");
    }

    // this method inserts a vehicle object to the database
    private void insertVehicleToDb(Statement statement, Vehicle vehicle) throws SQLException {
        statement.executeUpdate("INSERT INTO VEHICLES(make, category, rentalPrice, availableFor) " +
                "VALUES(" + vehicle.getMake() + ", " + vehicle.getCategory() + ", " + vehicle.getRentalPrice()
                + ", " + vehicle.isAvailableForRent() + ")");
    }

    private void createTables() throws SQLException {
        Statement statement = conn.createStatement();

        statement.executeUpdate("CREATE TABLE CUSTOMERS(custNumber AUTOINCREMENT PRIMARY KEY, " +
                "firstName VARCHAR(155) NOT NULL, surname VARCHAR(155) NOT NULL, idNum INTEGER NOT NULL, phoneNum INTEGER NOT NULL, canRent BIT NOT NULL)");
        statement.executeUpdate("CREATE TABLE VEHICLES(vehNumber AUTOINCREMENT PRIMARY KEY, make VARCHAR(155) NOT NULL, " +
                "category VARCHAR(155) NOT NULL, rentalPrice FLOAT NOT NULL, availableForRent BIT NOT NULL)");
        statement.executeUpdate("CREATE TABLE RENTALS(rentalNumber AUTOINCREMENT PRIMARY KEY, dateRental VARCHAR(155) NOT NULL, dateReturned VARCHAR(155) NOT NULL, " +
                "pricePerDay FLOAT NOT NULL, totalRental FLOAT NOT NULL, custNumber LONG FOREIGN KEY REFERENCES CUSTOMERS(custNumber), " +
                "vehNumber LONG FOREIGN KEY REFERENCES VEHICLES(vehNumber))");

        System.out.println("Database tables created");
    }

    private void connectToDatabase() throws SQLException {
        String dbURL = "jdbc:ucanaccess://src\\car_rentals.accdb";
        conn = DriverManager.getConnection(dbURL);
        System.out.println("Connection to database successful");
    }

    private void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("An error occurred: " + e);
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Waiting for client on port " +
                serverSocket.getLocalPort() + "...");

        while (true) {
            try (Socket server = serverSocket.accept()) {

                System.out.println("Connected to " + server.getRemoteSocketAddress());

                DataInputStream in = new DataInputStream(server.getInputStream());
                System.out.println(in.readUTF());

                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress());
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out");
                closeConnection();
                break;
            } catch (IOException e) {
                System.out.printf("An error occurred: %s%n", e);
                closeConnection();
            }
        }
    }
}
