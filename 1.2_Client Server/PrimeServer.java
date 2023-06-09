import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.*;

import rm.requestResponse.*;

import static java.lang.Thread.sleep;

public class PrimeServer extends Thread{
    private final static int PORT = 9090;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());

    private Component communication;
    private int port = PORT;

    public static int maxThreads = 8;

    PrimeServer(int port) {
        communication = new Component();
        if (port > 0) this.port = port;
    }

    void listen() {
        LOGGER.info("Listening on port " + port);

        CounterThread threadCounter = new CounterThread();
        threadCounter.start();

        while (true) {
            // dynmaisch -
            if (threadCounter.getThreadCounter() < maxThreads) {
            // dynmaisch -
                Long request = null;
                int sendPort = 0;

                // dynmaisch -
                if (threadCounter.getThreadCounter() < maxThreads-8) {
                    int newThreads = maxThreads - 8;
                    System.out.println("Lower threadpool from " + maxThreads + " to " + newThreads + "###########");
                    maxThreads -=8;
                }
                // dynmaisch -

                System.out.println("Receiving ...");

                long startTime = 0;
                try {
                    Message receivedMessage = communication.receive(port, true, false);
                    startTime = System.currentTimeMillis();
                    request = (Long) receivedMessage.getContent();
                    sendPort = receivedMessage.getPort();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                System.out.println((request.toString() + " received on port " + port));

                PrimeServiceThread primeServiceThread = new PrimeServiceThread();
                primeServiceThread.setNumber(request);
                primeServiceThread.setSendPort(sendPort);
                primeServiceThread.setThreadCounter(threadCounter);
                primeServiceThread.setStartWaitingTime(startTime);
                threadCounter.incThreadCounter();
//                System.out.println("Current amount of threads: " + threadCounter.getThreadCounter());

//          comment this in for constant thread pool
//                while (threadCounter >= 8) {
//                    System.out.println("Waiting for new thread...");
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//          comment

                primeServiceThread.start();
            // dynmaisch -
            } else {
                int newThreads = maxThreads + 8;
                System.out.println("Raising threadpool from " + maxThreads + " to " + newThreads + "###########");
                maxThreads +=8;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // dynmaisch -
        }
    }

    public void run() {
        listen();
    }

    public static void main(String[] args) {
        int port = 9090;

//        for (int i = 0; i < args.length; i++) {
//            switch (args[i]) {
//                case "-port":
//                    try {
//                        port = Integer.parseInt(args[++i]);
//                    } catch (NumberFormatException e) {
//                        LOGGER.severe("port must be an integer, not " + args[i]);
//                        System.exit(1);
//                    }
//                    break;
//                default:
//                    LOGGER.warning("Wrong parameter passed ... '" + args[i] + "'");
//            }
//        }
        String input;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("port [" + port + "] > ");
        try {
            input = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!input.equals("")) port = Integer.parseInt(input);


        new PrimeServer(port).listen();
    }
}
