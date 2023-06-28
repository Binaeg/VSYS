import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer implements BasicServer {

    private int port = 1234;
    private int connectionCount;

    private ServerListener listener;

    public RmiServer() {
        this.listener = new ServerListener(this);
        waitForConnection(port);
    }

    public String receiveMessage() {
        return this.listener.receiveMessage();
    }

    public void sendMessage(String message) {
        this.listener.sendMessage(message);
    }

    @Override
    public void waitForConnection(int port) {
        this.port = port;

        try {
            RmiClientListener clientListener = new RmiClientListener(this.listener);

            Remote clientListenerStub = (BasicClientListener) java.rmi.server.UnicastRemoteObject.exportObject(clientListener, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("clientListener", clientListenerStub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            while(true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setListener(BasicListener listener) {
        this.listener = (ServerListener) listener;
    }
}
