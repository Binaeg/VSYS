package BasicServer;

import java.io.*;
import java.net.*;

public class MySocketServerConnection extends Thread {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;
    static private int connectionCounter = 0;

    public MySocketServerConnection(Socket socket)
            throws IOException {
        this.socket = socket;
        objectOutputStream =
                new ObjectOutputStream(socket.getOutputStream());
        objectInputStream =
                new ObjectInputStream(socket.getInputStream());
        System.out.println("Server: incoming connection accepted.");
        name = "connection-" + connectionCounter++;
    }

    public void run() {

        try {
            while (true) {
                System.out.println("Server: waiting for message ...");
                String string = (String) objectInputStream.readObject();
                System.out.println("Server: received '" + string + "' from " + socket.getLocalSocketAddress() + " " + name);
                objectOutputStream.writeObject("server received " + string);

                if (string.equals("exit")) {
                    socket.close();
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
