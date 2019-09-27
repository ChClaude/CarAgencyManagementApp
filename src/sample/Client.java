package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private String serverName;
    private int port;

    Client(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;

        try (Socket client = new Socket(serverName, port)) {
            System.out.printf("Connecting to %s on port %d%n", serverName, port);
            System.out.printf("Connected to %s%n", client.getRemoteSocketAddress().toString());

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            out.writeUTF("This is from " + client.getLocalSocketAddress());

            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.printf("Server says %s%n", in.readUTF());
        } catch (IOException e) {
            System.out.printf("An error occurred: %s%n", e);
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
