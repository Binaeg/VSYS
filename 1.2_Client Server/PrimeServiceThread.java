import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

public class PrimeServiceThread extends Thread {

    private long number;

    private int sendPort;

    private BasicConnection connection;

    private RmiServer rmiServer;

    public BasicConnection getConnection() {
        return connection;
    }

    public void setConnection(BasicConnection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        boolean res = primeService(number);
//        Message message = new Message("localhost", sendPort, res);
//        Component communication = new Component();
//            communication.send(message, sendPort, true);
        rmiServer.sendMessage(String.valueOf(res));
        PrimeServer.threadCounter--;
//        System.out.println("Current amount of threads: " + PrimeServer.threadCounter);
    }

    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    public void setRmiServer(RmiServer rmiServer) {
        this.rmiServer = rmiServer;
    }
}
