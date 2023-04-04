import rm.requestResponse.Component;

import java.io.IOException;

public class PrimeThread extends Thread {

    private long value;
    private Component communication;
    private String hostname;
    private int port;

    @Override
    public void run() {
        try {
            PrimeClient.processNumberSync(value, hostname, port, communication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setCommunication(Component communication) {
        this.communication = communication;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PrimeThread(long value, Component component, String hostname, int port) {
        this.value = value;
        this.communication = component;
        this.hostname = hostname;
        this.port = port;
    }
}
