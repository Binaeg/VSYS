import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rm.requestResponse.*;

import static java.lang.Thread.sleep;

public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final boolean REQUESTMODE = true;

    private static final boolean MULTI_THREADING = true;

//    private Component communication;
    private RmiClient rmiClient;

    String hostname;
    int sendPort, receivePort;
    long initialValue, count;
    boolean requestMode;
    boolean multiThreading;

    public PrimeClient(String hostname, int sendPort, int receivePort, long initialValue, long count, boolean requestMode, boolean multiThreading) {
        this.hostname = hostname;
        this.sendPort = sendPort;
        this.receivePort = receivePort;
        this.initialValue = initialValue;
        this.count = count;
        this.requestMode = requestMode;
        this.multiThreading = multiThreading;
    }

    public void run() {
//        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            try {
                this.rmiClient = new RmiClient();
                rmiClient.connect(PORT, hostname);
                processNumber(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void processNumber(long value) throws IOException {
        Message message = new Message(hostname, receivePort, value);
//        communication.send(message, sendPort, false);
        rmiClient.sendMessage(String.valueOf(value));
        Boolean isPrime = false;
        Boolean received = false;
        while (!received) {
            try {
                // better: null check -> performance
//                isPrime = (Boolean) communication.receive(receivePort, requestMode, true).getContent();
                isPrime = Boolean.parseBoolean(rmiClient.receiveMessage());
                received = true;
            } catch (Exception e) {
//                System.out.print(".");
            }
            try {
                sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("client" + receivePort + " " + value + ": " + (isPrime.booleanValue() ? " prime" : " not prime"));
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname = HOSTNAME;
        int port = PORT;
        long initialValue = INITIAL_VALUE;
        long count = COUNT;
        boolean requestMode = REQUESTMODE;
        boolean multiThreading = MULTI_THREADING;

        boolean doExit = false;

        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to " + CLIENT_NAME + "\n");

        while (!doExit) {
            System.out.print("Server hostname [" + hostname + "] > ");
            input = reader.readLine();
            if (!input.equals("")) hostname = input;

            System.out.print("Server port [" + port + "] > ");
            input = reader.readLine();
            if (!input.equals("")) port = Integer.parseInt(input);

            System.out.print("Requestmode non-blocking? y|n [n] > ");
            input = reader.readLine();
            if (input.equals("y")) {
                requestMode = true;
            } else {
                requestMode = false;
            }

            System.out.print("Multi Threading? y|n [n] > ");
            input = reader.readLine();
            if (input.equals("y")) {
                multiThreading = true;
            } else {
                multiThreading = false;
            }

            System.out.print("Prime search initial value [" + initialValue + "] > ");
            input = reader.readLine();
            if (!input.equals("")) initialValue = Integer.parseInt(input);

            System.out.print("Prime search count [" + count + "] > ");
            input = reader.readLine();
            if (!input.equals("")) count = Integer.parseInt(input);


            new PrimeClient(hostname, port, 1, initialValue, count, requestMode, multiThreading).start();

            System.out.println("Exit [n]> ");
            input = reader.readLine();
            if (input.equals("y") || input.equals("j")) doExit = true;
        }
    }
}