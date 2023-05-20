import java.io.IOException;
import java.util.logging.*;

import rm.requestResponse.*;

import static java.lang.Thread.sleep;

public class PrimeServer {
    private final static int PORT = 9090;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());

    private Component communication;
    private int port = PORT;

    public static int threadCounter = 0;

    PrimeServer(int port) {
        communication = new Component();
        if (port > 0) this.port = port;
    }

    void listen() {
        LOGGER.info("Listening on port " + port);

        while (true) {
            Long request = null;
            int sendPort = 0;

            System.out.println("Receiving ...");
            try {
                Message receivedMessage = communication.receive(port, true, false);
                request = (Long) receivedMessage.getContent();
                sendPort = receivedMessage.getPort();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            System.out.println((request.toString() + " received on port " + sendPort));

            PrimeServiceThread primeServiceThread = new PrimeServiceThread();
            primeServiceThread.setNumber(request);
            primeServiceThread.setSendPort(sendPort);
            threadCounter++;
            System.out.println("Current amount of threads: " + threadCounter);

//          comment this in for constant thread pool
            while (threadCounter >= 8) {
                System.out.println("Waiting for new thread...");
                try {
                    sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
//          end of constant thread pool


            primeServiceThread.start();
        }
    }

    public static void main(String[] args) {
        int port = 0;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-port":
                    try {
                        port = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException e) {
                        LOGGER.severe("port must be an integer, not " + args[i]);
                        System.exit(1);
                    }
                    break;
                default:
                    LOGGER.warning("Wrong parameter passed ... '" + args[i] + "'");
            }
        }

        new PrimeServer(port).listen();
    }
}
