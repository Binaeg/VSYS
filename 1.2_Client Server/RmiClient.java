import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

public class RmiClient implements BasicClient{
    private BasicClientConnection connection;
    private String incomingMessage;
    private String outgoingMessage;

    public void connect(int port, String hostname) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, port);
            BasicClientListener listener = (BasicClientListener) registry.lookup("clientListener");
            connection = listener.connect();
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String receiveMessage() {
        if (!Objects.equals(incomingMessage, "")) {
            outgoingMessage = incomingMessage;
            incomingMessage = "";
        }

        return outgoingMessage;
    }

    public void sendMessage(String text) {
        try {
            incomingMessage = connection.message(text);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
