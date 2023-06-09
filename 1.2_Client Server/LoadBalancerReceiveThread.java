import rm.requestResponse.Component;
import rm.requestResponse.Message;
import rm.serverAdmin.ServerAdmin;
import rm.serverAdmin.ServerConfig;

import java.io.IOException;

public class LoadBalancerReceiveThread extends Thread{

    private int receivePort;

    private int sendPort;

    private Component communication;

    private ServerAdmin serverAdmin;

    private ServerConfig serverConfig;

    public void run() {

        communication = new Component();

        while(true) {
            try {
                Message receivedMessage = communication.receive(receivePort, true, false);
                Response response = (Response) receivedMessage.getContent();

                Message sendMessage = new Message("localhost",0, response);
                communication.send(sendMessage, sendPort, false);

                serverAdmin.release(serverConfig);

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getReceivePort() {
        return receivePort;
    }

    public int getSendPort() {
        return sendPort;
    }

    public ServerAdmin getServerAdmin() {
        return serverAdmin;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setReceivePort(int receivePort) {
        this.receivePort = receivePort;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    public void setServerAdmin(ServerAdmin serverAdmin) {
        this.serverAdmin = serverAdmin;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}
