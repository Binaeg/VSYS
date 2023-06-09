import rm.requestResponse.Component;
import rm.requestResponse.Message;
import rm.serverAdmin.ServerAdmin;
import rm.serverAdmin.ServerConfig;

import java.io.IOException;

public class LoadBalancer {

    int counter;

    static int port = 3000;

    private static Component communication;

    public static void main(String[] args) throws IOException {

        ServerAdmin server = new ServerAdmin("/home/timm/studium/vsys/Git/VSYS2/VSYS/1.2_Client Server/config.txt");


        communication = new Component();

        while (true) {
                Long request = null;
                int sendPort = 0;

                System.out.println("Receiving ...");

                try {
                    Message receivedMessage = communication.receive(port, true, false);
                    request = (Long) receivedMessage.getContent();
                    sendPort = receivedMessage.getPort();

                    ServerConfig serverConfig = server.bind();

                    Message sendMessage = new Message(serverConfig.getHostname(), serverConfig.getReceivePort(), request);
                    communication.send(sendMessage, serverConfig.getSendPort(), false);

                    LoadBalancerReceiveThread receiveThread = new LoadBalancerReceiveThread();
                    receiveThread.setReceivePort(serverConfig.getReceivePort());
                    receiveThread.setSendPort(serverConfig.getSendPort());
                    receiveThread.setServerAdmin(server);
                    receiveThread.setServerConfig(serverConfig);

                    receiveThread.start();


                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                System.out.println((request.toString() + " received on port " + sendPort));




        }


    }
}
