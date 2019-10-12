package sample;

import project_classes.Customer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private String serverName;
    private int port;

    Client(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;

        try (Socket client = new Socket(serverName, port)) {
            System.out.printf("Connecting to %s on port %d%n", serverName, port);
            System.out.printf("Connected to %s%n", client.getRemoteSocketAddress().toString());

            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.writeObject("This is from " + client.getLocalSocketAddress());

            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            System.out.printf("Server says %s%n", in.readObject());
        } catch (IOException e) {
            System.out.printf("An error occurred: %s%n", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    String writeCustomerToServer(Customer customer) {
        String response = "";
        try (Socket client = new Socket(serverName, port)) {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.writeObject(customer);

            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            do {
                response = (String) in.readObject();
            } while (!(response.equals("Customer added") || response.equals("An error occurred while adding customer")));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return String.format("Client = {server name: %s, port: %d}",
                serverName, port);
    }
}
