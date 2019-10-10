package sample.server_side;

import project_classes.Customer;
import project_classes.Vehicle;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private Connection conn = null;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(7000);

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
                Paths.get("Customers.ser")));

             ObjectInputStream vehicleInputStream = new ObjectInputStream(Files.newInputStream(
                     Paths.get("Vehicles.ser")));
             Statement statement = conn.createStatement()) {

            getDataFromCustomerSeqFileToDb(customerInputStream, statement);
            getDataFromVehicleSeqFileToDb(vehicleInputStream, statement);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Data loaded from files to database");
    }

    private void getDataFromCustomerSeqFileToDb(ObjectInputStream customerInputStream, Statement statement) throws IOException, SQLException {
        int columns = 0;
        try {
            while (true) {
                // loading customers to database
                Customer customer = (Customer) customerInputStream.readObject();
                insertCustomerToDb(statement, customer);
                columns++;
            }
        } catch (EOFException ex) {
            System.out.println(columns + " affected in table CUSTOMERS.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }
    }

    private void getDataFromVehicleSeqFileToDb(ObjectInputStream vehicleInputStream, Statement statement) throws IOException, SQLException {
        int columns = 0;
        try {
            while (true) {
                // loading vehicles to database
                Vehicle vehicle = (Vehicle) vehicleInputStream.readObject();
                insertVehicleToDb(statement, vehicle);
                columns++;
            }
        } catch (EOFException ex) {
            System.out.println(columns + " affected in table VEHICLES.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }
    }

    // this method inserts a customer object to the database
    private void insertCustomerToDb(Statement statement, Customer customer) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT custNumber FROM CUSTOMERS WHERE custNumber=" +
                customer.getIdNum() + ";");

        long index = -1L;
        while (resultSet.next())
            index = resultSet.getLong(1);

        if (index == -1)
            statement.executeUpdate("INSERT INTO CUSTOMERS " +
                    "VALUES('" + customer.getIdNum() + "', '" + customer.getName() + "', '" + customer.getSurname() + "', '"
                     + customer.getPhoneNum() + "', " + customer.canRent() + ")");
    }

    // this method inserts a vehicle object to the database
    private void insertVehicleToDb(Statement statement, Vehicle vehicle) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT vehNumber FROM VEHICLES WHERE vehNumber=" +
                vehicle.getVehNumber() + ";");

        long index = -1L;
        while (resultSet.next())
            index = resultSet.getInt(1);

        if (index == -1)
            statement.executeUpdate("INSERT INTO VEHICLES " +
                "VALUES(" + vehicle.getVehNumber() + ", '" + vehicle.getMake() + "', '" + vehicle.getCategory() + "', " + vehicle.getRentalPrice()
                + ", " + vehicle.isAvailableForRent() + ")");
    }

    private void createTables() throws SQLException {
        final String RENTALS = "RENTALS";
        Statement statement = conn.createStatement();

        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTables(null, null, RENTALS, null);

        while (rs.next()) {
            if (rs.getString(3).equals(RENTALS))
                return;
        }

        statement.executeUpdate("CREATE TABLE CUSTOMERS(custNumber VARCHAR(255) PRIMARY KEY, " +
                "firstName VARCHAR(155), surname VARCHAR(155), phoneNum " +
                "VARCHAR(200), canRent BOOLEAN);");

        statement.executeUpdate("CREATE TABLE VEHICLES(vehNumber LONG PRIMARY KEY, make VARCHAR(155), " +
                "category VARCHAR(155), rentalPrice FLOAT, availableForRent BOOLEAN);");

        statement.executeUpdate("CREATE TABLE RENTALS(rentalNumber AUTOINCREMENT PRIMARY KEY, dateRental VARCHAR(155), " +
                "dateReturned VARCHAR(155), pricePerDay FLOAT, totalRental FLOAT, " +
                "custNumber VARCHAR(255), vehNumber INT);");
        statement.executeUpdate("ALTER TABLE RENTALS ADD FOREIGN KEY (custNumber) REFERENCES CUSTOMERS(custNumber);");
        statement.executeUpdate("ALTER TABLE RENTALS ADD FOREIGN KEY (vehNumber) REFERENCES VEHICLES(vehNumber);");

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
