import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiPrimeServerInterface extends Remote {

    public boolean isPrim(long candidate)
        throws RemoteException;
}
