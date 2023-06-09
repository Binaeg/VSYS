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

        String timm = "/home/timm/studium/vsys/Git/VSYS2/VSYS/1.2_Client Server/config.txt";
        String simon = "D:\\GitHub\\VSYS\\1.2_Client Server\\config.txt";
        ServerAdmin server = new ServerAdmin(simon);


        communication = new Component();

        while (true) {
            Long request = null;
            int sendPort = 0;

            System.out.println("Receiving ...");

            try {
                //receive message from client
                Message receivedMessage = communication.receive(port, true, false);
                request = (Long) receivedMessage.getContent();
                sendPort = receivedMessage.getPort();
                System.out.println((request.toString() + " received on port " + port));

                //get free server configuration
                ServerConfig serverConfig = server.bind();

                //send message to free server configuration
                Message sendMessage = new Message(serverConfig.getHostname(), serverConfig.getReceivePort(), request);
                communication.send(sendMessage, serverConfig.getSendPort(), false);
                communication.cleanup();

                //start new thread waiting for the response
                LoadBalancerReceiveThread receiveThread = new LoadBalancerReceiveThread();
                receiveThread.setServerReceivePort(serverConfig.getReceivePort());
                receiveThread.setClientSendPort(sendPort);
                receiveThread.setServerAdmin(server);
                receiveThread.setServerConfig(serverConfig);

                receiveThread.start();


            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }


        }


    }
}
