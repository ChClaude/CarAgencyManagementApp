package sample.server_side;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server extends Thread {

    private ServerSocket serverSocket;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(15000);
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
                break;
            } catch (IOException e) {
                System.out.printf("An error occurred: %s%n", e);
            }
        }
    }
}
