import rm.requestResponse.Component;
import rm.requestResponse.Message;
import rm.serverAdmin.ServerAdmin;
import rm.serverAdmin.ServerConfig;

import java.io.IOException;

public class LoadBalancerReceiveThread extends Thread{

    private int serverReceivePort;

    private int clientSendPort;

    private Component communication;

    private ServerAdmin serverAdmin;

    private ServerConfig serverConfig;

    public void run() {

        communication = new Component();

        boolean received = false;

        while(!received) {
            try {
                Message receivedMessage = communication.receive(serverReceivePort, true, false);
                Response response = (Response) receivedMessage.getContent();

                Message sendMessage = new Message("localhost",0, response);
                communication.send(sendMessage, clientSendPort, false);
                received = true;

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    communication.cleanup();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                serverAdmin.release(serverConfig);
            }
        }
    }

    public int getServerReceivePort() {
        return serverReceivePort;
    }

    public int getClientSendPort() {
        return clientSendPort;
    }

    public ServerAdmin getServerAdmin() {
        return serverAdmin;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerReceivePort(int serverReceivePort) {
        this.serverReceivePort = serverReceivePort;
    }

    public void setClientSendPort(int clientSendPort) {
        this.clientSendPort = clientSendPort;
    }

    public void setServerAdmin(ServerAdmin serverAdmin) {
        this.serverAdmin = serverAdmin;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}
