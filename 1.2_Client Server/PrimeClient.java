import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rm.requestResponse.*;

import static java.lang.Thread.sleep;

public class PrimeClient extends Thread {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 9090;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final boolean REQUESTMODE = true;

    private static final boolean MULTI_THREADING = true;

    private Component communication;
    String hostname;
    int sendPort, receivePort;
    long initialValue, count;
    boolean requestMode;
    boolean multiThreading;

    List<Long> processingTimes = new ArrayList<>();

    List<Long> waitingTimes = new ArrayList<>();

    List<Long> communicationTimes = new ArrayList<>();

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
        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            try {
                processNumber(i);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void processNumber(long value) throws IOException {
        long startTime = System.currentTimeMillis();
        Message message = new Message(hostname, receivePort, value);
        communication.send(message, sendPort, false);
        Boolean isPrime = false;
        long processingTime = 0;
        long waitingTime = 0;
        long communicationTime = 0;
        Boolean received = false;
        while (!received) {
            try {
                Response response = (Response) communication.receive(receivePort, requestMode, true).getContent();                // better: null check -> performance
                isPrime = response.isPrime();
                processingTime = response.getProcessingTime();
                waitingTime = response.getWaitingTime();
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
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        communicationTime = totalTime - processingTime - waitingTime;

        processingTimes.add(processingTime);
        waitingTimes.add(waitingTime);
        communicationTimes.add(communicationTime);

        long avgProcessingTime = averageCalc(processingTimes);
        long avgWaitingTime = averageCalc(waitingTimes);
        long avgCommunicationTime = averageCalc(communicationTimes);



        System.out.println("client" + receivePort + " " + value + ": " + (isPrime.booleanValue() ? " prime" : " not prime") + " | p: " + processingTime + "(" + avgProcessingTime + ")" + " ms | w: " + waitingTime + "(" + avgWaitingTime + ")" + " ms | c: " + communicationTime +  "(" + avgCommunicationTime + ")" + " ms");
    }

    public long averageCalc(List<Long> list) {
        long sum = 0;
        if(!list.isEmpty()) {
            for (long mark : list) {
                sum += mark;
            }
            return sum / list.size();
        }
        return sum;
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