import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer implements BasicServer {

    private Remote clientListenerStub;
    private int port;
    private int connectionCount;

    private BasicListener listener;

    @Override
    public void waitForConnection(int port, PrimeServer listener) {
        this.port = port;

        try {
            RmiClientListener clientListener = new RmiClientListener(this.listener);

            clientListenerStub = (BasicClientListener) java.rmi.server.UnicastRemoteObject.exportObject(clientListener, 0);
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
//        clientListener.setListener(listener);
        this.listener = listener;
    }
}
