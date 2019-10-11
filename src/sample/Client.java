package sample;

import java.io.*;
import java.net.Socket;

public class Client {

    private String serverName;
    private int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    Client(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;

        try (Socket client = new Socket(serverName, port)) {
            System.out.printf("Connecting to %s on port %d%n", serverName, port);
            System.out.printf("Connected to %s%n", client.getRemoteSocketAddress().toString());

            out = new ObjectOutputStream(client.getOutputStream());
            out.writeObject("This is from " + client.getLocalSocketAddress());

            in = new ObjectInputStream(client.getInputStream());
            System.out.printf("Server says %s%n", in.readObject());
        } catch (IOException e) {
            System.out.printf("An error occurred: %s%n", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeToServer(Object o) {
        try {
            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
