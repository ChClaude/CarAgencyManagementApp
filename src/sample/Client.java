package sample;

import project_classes.Customer;
import project_classes.Rental;
import project_classes.Vehicle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client {

    private String serverName;
    private int port;
    private Object[] data;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    Client(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    void runClient() {
        try (Socket client = new Socket(serverName, port)) {
            System.out.printf("Connecting to %s on port %d%n", serverName, port);
            System.out.printf("Connected to %s%n", client.getRemoteSocketAddress().toString());

            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            data = (Object[]) in.readObject();
            out.writeObject("This is from " + client.getLocalSocketAddress());
        } catch (IOException e) {
            System.out.printf("An error occurred: %s%n", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    List<Customer> getCustomers() {
        return (List<Customer>) data[0];
    }

    List<Vehicle> getVehicles() {
        return (List<Vehicle>) data[1];
    }

    List<Rental> getRentals() {
        return (List<Rental>) data[2];
    }

    String writeCustomerToServer(Customer customer) {
        String response = "response";
        try {
            out.writeObject(customer);
            response = in.readObject() instanceof String ? (String) in.readObject() : "Response type is not a text";
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public String toString() {
        return String.format("Client = {server name: %s, port: %d}",
                serverName, port);
    }
}
