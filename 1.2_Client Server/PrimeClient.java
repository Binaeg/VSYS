import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rm.requestResponse.*;

import static java.lang.Thread.sleep;

public class PrimeClient {
    private static final String HOSTNAME="localhost";
    private static final int PORT=1234;
    private static final long INITIAL_VALUE=(long)1e17;
    private static final long COUNT=20;
    private static final String CLIENT_NAME=PrimeClient.class.getName();
    private static final boolean REQUESTMODE = true;

    private static final boolean MULTI_THREADING = true;

    private Component communication;
    String hostname;
    int port;
    long initialValue,count;
    boolean requestMode;
    boolean multiThreading;

    public PrimeClient(String hostname,int port,long initialValue,long count, boolean requestMode, boolean multiThreading) {
        this.hostname=hostname;
        this.port=port;
        this.initialValue=initialValue;
        this.count=count;
        this.requestMode=requestMode;
        this.multiThreading=multiThreading;
    }

    public void execute() throws IOException {
        communication=new Component();
        if (multiThreading) {
            for (long i=initialValue;i<initialValue+count;i++) {
                // create thread for every number i
                new PrimeThread(i, communication, hostname, port).start();
            }
        } else {
            for (long i=initialValue;i<initialValue+count;i++) processNumber(i);
        }
    }

    public static synchronized void processNumberSync(long value, String hostname, int port, Component communication) throws IOException {
        communication.send(new Message(hostname, port, new Long(value)), false);
        Boolean isPrime = false;
        Boolean received = false;
        System.out.print(value + ": ");
        while (!received) {
            try {
                sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                isPrime = (Boolean) communication.receive(port, true, true).getContent();
                received = true;
            } catch (Exception e) {
                System.out.print(".");
            }
        }

        System.out.println((isPrime.booleanValue() ? " prime" : " not prime"));
    }

    public void processNumber(long value) throws IOException {
        communication.send(new Message(hostname, port, new Long(value)), false);
        Boolean isPrime = false;
        Boolean received = false;
        System.out.print(value + ": ");
        while (!received) {
            try {
                sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                // better: null check -> performance
                isPrime = (Boolean) communication.receive(port, requestMode, true).getContent();
                received = true;
            } catch (Exception e) {
                System.out.print(".");
            }
        }

        System.out.println((isPrime.booleanValue() ? " prime" : " not prime"));
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        String hostname=HOSTNAME;
        int port=PORT;
        long initialValue=INITIAL_VALUE;
        long count=COUNT;
        boolean requestMode=REQUESTMODE;
        boolean multiThreading=MULTI_THREADING;

        boolean doExit=false;

        String input;
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in ));

        System.out.println("Welcome to "+CLIENT_NAME+"\n");

        while(!doExit) {
            System.out.print("Server hostname ["+hostname+"] > ");
            input=reader.readLine();
            if(!input.equals("")) hostname=input;

            System.out.print("Server port ["+port+"] > ");
            input=reader.readLine();
            if(!input.equals("")) port=Integer.parseInt(input);

            System.out.print("Requestmode non-blocking? y|n [n] > ");
            input=reader.readLine();
            if(input.equals("y")) {
                requestMode=true;
            } else {
                requestMode=false;
            }

            System.out.print("Multi Threading? y|n [n] > ");
            input=reader.readLine();
            if (input.equals("y")) {
                multiThreading=true;
            } else {
                multiThreading=false;
            }

            System.out.print("Prime search initial value ["+initialValue+"] > ");
            input=reader.readLine();
            if(!input.equals("")) initialValue=Integer.parseInt(input);

            System.out.print("Prime search count ["+count+"] > ");
            input=reader.readLine();
            if(!input.equals("")) count=Integer.parseInt(input);


            new PrimeClient(hostname,port,initialValue,count,requestMode,multiThreading).execute();

            System.out.println("Exit [n]> ");
            input=reader.readLine();
            if(input.equals("y") || input.equals("j")) doExit=true;
        }
    }
}