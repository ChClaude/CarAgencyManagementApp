package sample.server_side;

import project_classes.Customer;
import project_classes.Rental;
import project_classes.Vehicle;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private Connection conn = null;


    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        populateDatabase();

    }

    @Override
    public void run() {
        System.out.println("Waiting for client on port " +
                serverSocket.getLocalPort() + "...");

        while (true) {
            try (Socket server = serverSocket.accept()) {

                System.out.println("Connected to " + server.getRemoteSocketAddress());

                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                out.writeObject(retrieveDataFromDb());

                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                Object object = in.readObject();

                if (object instanceof Customer) {
                    Customer customer = (Customer) object;
                    boolean isInserted = insertCustomerToDb(conn.createStatement(), customer);
                    if (isInserted)
                        out.writeObject("Customer added");
                    else
                        out.writeObject("An error occurred while adding customer");
                } else {
                    System.out.println(object);
                }
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out");
                closeConnection();
                break;
            } catch (IOException e) {
                System.out.printf("An error occurred: %s%n", e);
                closeConnection();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Object[] retrieveDataFromDb() throws SQLException {
        Object[] objects = new Object[3];
        List<Customer> customers = new ArrayList<>();
        List<Vehicle> vehicles = new ArrayList<>();
        List<Rental> rentals = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet custResults = statement.executeQuery("SELECT * FROM CUSTOMERS");
        while (custResults.next()) {
            Customer customer = new Customer(custResults.getString("firstName"), custResults.getString("surname"),
                    custResults.getString("custNumber"), custResults.getString("phoneNum"), custResults.getBoolean("canRent"));
            customers.add(customer);
        }

        ResultSet vehResults = statement.executeQuery("SELECT * FROM VEHICLES");
        while (vehResults.next()) {
            Vehicle vehicle = new Vehicle(vehResults.getInt("vehNumber"), vehResults.getString("make"),
                    vehResults.getString("category").equals("Sedan") ? 1 : 0,
                    vehResults.getDouble("rentalPrice"), vehResults.getBoolean("availableForRent"));
            vehicles.add(vehicle);
        }

        ResultSet rentResults = statement.executeQuery("SELECT * FROM RENTALS");
        while (rentResults.next()) {
            Rental rental = new Rental(rentResults.getInt("rentalNumber"), rentResults.getString("dateRental"),
                    rentResults.getString("dateReturned"), rentResults.getDouble("pricePerDay"),
                    rentResults.getDouble("pricePerDay"), rentResults.getInt("custNumber"),
                    rentResults.getInt("vehNumber"));
            rentals.add(rental);
        }

        objects[0] = customers;
        objects[1] = vehicles;
        objects[2] = rentals;

        return objects;
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
    }

    private void getDataFromCustomerSeqFileToDb(ObjectInputStream customerInputStream, Statement statement) throws IOException, SQLException {
        int columns = 0;
        try {
            while (true) {
                // loading customers to database
                Customer customer = (Customer) customerInputStream.readObject();
                if (!insertCustomerToDb(statement, customer)) {
                    System.out.println("This data has already been recorded in table CUSTOMER.");
                    return;
                }
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
                if (!insertVehicleToDb(statement, vehicle)) {
                    System.out.println("This data has already been recorded in table VEHICLE.");
                    return;
                }

                columns++;
            }
        } catch (EOFException ex) {
            System.out.println(columns + " affected in table VEHICLES.");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }
    }

    // this method checks whether the object to be inserted is recorded already (returns false in this case)
    // if not it inserts it to the customer table (returns true in this case)
    private boolean insertCustomerToDb(Statement statement, Customer customer) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT custNumber FROM CUSTOMERS WHERE custNumber='" +
                customer.getIdNum() + "';");

        String index = "";
        while (resultSet.next())
            index = resultSet.getString(1);

        if (index.equals("")) {
            statement.executeUpdate("INSERT INTO CUSTOMERS " +
                    "VALUES('" + customer.getIdNum() + "', '" + customer.getName() + "', '" + customer.getSurname() + "', '"
                    + customer.getPhoneNum() + "', " + customer.canRent() + ")");
            return true;
        }

        return false;
    }

    // this method checks whether the object to be inserted is recorded already (returns true in this case)
    // if not it inserts it to the vehicle table (returns true in this case)
    private boolean insertVehicleToDb(Statement statement, Vehicle vehicle) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT vehNumber FROM VEHICLES WHERE vehNumber=" +
                vehicle.getVehNumber() + ";");

        long index = -1L;
        while (resultSet.next())
            index = resultSet.getInt(1);

        if (index == -1) {
            statement.executeUpdate("INSERT INTO VEHICLES " +
                    "VALUES(" + vehicle.getVehNumber() + ", '" + vehicle.getMake() + "', '" + vehicle.getCategory() + "', " + vehicle.getRentalPrice()
                    + ", " + vehicle.isAvailableForRent() + ")");

            return true;
        }

        return false;
    }

    private void createTables() throws SQLException {
        final String RENTALS = "RENTALS";
        Statement statement = conn.createStatement();

        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTables(null, null, RENTALS, null);

        while (rs.next()) {
            if (rs.getString(3).equals(RENTALS)) {
                System.out.println("Tables (Customers, Vehicles, Rentals) already existent in the database");
                return;
            }
        }

        statement.executeUpdate("CREATE TABLE CUSTOMERS(custNumber VARCHAR(255) PRIMARY KEY, " +
                "firstName VARCHAR(155) NOT NULL, surname VARCHAR(155) NOT NULL, phoneNum " +
                "VARCHAR(200) NOT NULL, canRent BOOLEAN NOT NULL);");

        statement.executeUpdate("CREATE TABLE VEHICLES(vehNumber LONG PRIMARY KEY, make VARCHAR(155) NOT NULL, " +
                "category VARCHAR(155) NOT NULL, rentalPrice FLOAT NOT NULL, availableForRent BOOLEAN NOT NULL);");

        statement.executeUpdate("CREATE TABLE RENTALS(rentalNumber AUTOINCREMENT PRIMARY KEY, dateRental VARCHAR(155) NOT NULL, " +
                "dateReturned VARCHAR(155), pricePerDay FLOAT NOT NULL, totalRental FLOAT, " +
                "custNumber VARCHAR(255) NOT NULL, vehNumber INT NOT NULL);");
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

}
