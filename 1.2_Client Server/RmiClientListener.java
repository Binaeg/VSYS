import java.rmi.RemoteException;

public class RmiClientListener implements BasicClientListener {
    private RmiClientConnection clientConnection;
    private BasicClientConnection clientConnectionStub;
    private BasicListener listener;
    private int connectionCount;

    public RmiClientListener(BasicListener listener) {
        this.listener = listener;
        connectionCount = 0;
    }


    @Override
    public BasicClientConnection connect() throws RemoteException {
        try {
            clientConnection = new RmiClientConnection(++connectionCount, listener);
            clientConnection.start();
            clientConnectionStub = (BasicClientConnection) java.rmi.server.UnicastRemoteObject.exportObject(clientConnection, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return clientConnectionStub;
    }

    public void setListener(BasicListener listener) {
        this.listener = listener;
    }
}
