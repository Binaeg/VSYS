import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BasicClientListener extends Remote {
    BasicClientConnection connect() throws RemoteException;
}
