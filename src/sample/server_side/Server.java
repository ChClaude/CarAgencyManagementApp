package sample.server_side;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
        } catch (SQLException e) {
            System.err.println("An error occurred " + e);
        }

    }

    private void createTables() throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("CREATE TABLE CUSTOMERS(custNumber AUTOINCREMENT PRIMARY KEY, " +
                "firstName VARCHAR(155) NOT NULL, surname VARCHAR(155) NOT NULL, idNum INTEGER NOT NULL, phoneNum INTEGER NOT NULL, canRent BIT NOT NULL)");
        statement.executeUpdate("CREATE TABLE VEHICLES(vehNumber AUTOINCREMENT PRIMARY KEY, make VARCHAR(155) NOT NULL, " +
                "category VARCHAR(155) NOT NULL, rentalPrice FLOAT NOT NULL, availableForRent BIT NOT NULL)");
        statement.executeUpdate("CREATE TABLE RENTALS(rentalNumber AUTOINCREMENT PRIMARY KEY, dateRental VARCHAR(155) NOT NULL, dateReturned VARCHAR(155) NOT NULL, " +
                "pricePerDay FLOAT NOT NULL, totalRental FLOAT NOT NULL, custNumber INTEGER FOREIGN KEY REFERENCES CUSTOMERS(custNumber), " +
                "vehNumber INTEGER FOREIGN KEY REFERENCES VEHICLES(vehNumber))");

        System.out.println("Database populated");
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
