import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

public class PrimeServiceThread extends Thread {

    private long number;
    private Component communication;

    private int port;
    @Override
    public void run() {
        boolean res = true;
        for (long i = 2; i < Math.sqrt(number)+1; i++) {
            if (number % i == 0) {
                res = false;
                break;
            }
        }

        Message message = new Message("localhost", port, res);
        try {
            communication.send(message, port, true);
            PrimeServer.threadCounter--;
            communication.cleanup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Component getCommunication() {
        return communication;
    }

    public void setCommunication(Component communication) {
        this.communication = communication;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
