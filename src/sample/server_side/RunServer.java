package sample.server_side;

import java.io.IOException;

public class RunServer {

    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server(8085);
            server.start();
        } catch (IOException e) {
            System.out.println("An error occurred " + e);
        }
    }

}
