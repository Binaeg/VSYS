import java.rmi.RemoteException;

public class RmiClientConnection extends Thread implements BasicClientConnection, BasicConnection{

    private BasicListener listener;
    private String incomingMessage;
    private String incomingMessageCopy;
    private String outgoingMessage;
    private String outgoingMessageCopy;
    private boolean messageReceived;
    private boolean messageToSend;
    private int id;

    private Integer monitor;

    public RmiClientConnection(int id, BasicListener listener) {
        this.id = id;
        this.listener = listener;
        messageReceived = false;
        messageToSend = false;
        monitor = 0;
    }

    public void run() {
        listener.connectionAccepted(this);
    }

    @Override
    public String message(String text) throws RemoteException {
        if (messageReceived) {
            synchronized (monitor) {
                try {
                    monitor.notify();
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        incomingMessage = text;
        messageReceived = true;

        if (!messageToSend) {
            synchronized (monitor) {
                try {
                    monitor.notify();
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        outgoingMessageCopy = outgoingMessage;
        messageToSend = false;

        return outgoingMessageCopy;
    }

    @Override
    public void disconnect() throws RemoteException {

    }

    @Override
    public String receiveMessage() {
        if (!messageReceived) {
            synchronized (monitor) {
                try {
                    monitor.notify();
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        incomingMessageCopy = incomingMessage;
        messageReceived = false;

        return incomingMessageCopy;
    }

    @Override
    public void sendMessage(String message) {
        outgoingMessage = message;
        messageToSend = true;

        synchronized (monitor) {
            try {
                monitor.notify();
                monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {

    }
}
