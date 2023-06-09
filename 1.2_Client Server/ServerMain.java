import rm.serverAdmin.ServerAdmin;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) throws IOException {

        int port = 2100;

        for (int i = 1; i < 4; i++) {
            PrimeServer primeServer = new PrimeServer(port++);
            primeServer.start();
        }
    }

}
