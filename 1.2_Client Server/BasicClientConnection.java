import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BasicClientConnection extends Remote {
    String message(String text) throws RemoteException;
    void disconnect() throws RemoteException;
}
