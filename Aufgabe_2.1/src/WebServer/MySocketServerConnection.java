package WebServer;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class MySocketServerConnection extends Thread {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public MySocketServerConnection(Socket socket)
            throws IOException {
        this.socket = socket;
        objectOutputStream =
                new ObjectOutputStream(socket.getOutputStream());
        objectInputStream =
                new ObjectInputStream(socket.getInputStream());
        System.out.println("Server: incoming connection accepted.");
    }

    public void run() {

        try {
            while (true) {
                System.out.println("Server: waiting for message ...");
                String string = (String) objectInputStream.readObject();
                System.out.println("Server: received '" + string + "' from " + socket.getLocalSocketAddress());
//                string = string.replaceAll("/", "\\");
                try {
                    Path filePath = Path.of("D:\\OneDrive\\Studium\\HTWG\\06_SS23\\VSYS\\Tasks\\Aufgabe_2.1\\src\\pages" + string);

                    String content = Files.readString(filePath);
                    objectOutputStream.writeObject(content);
                } catch (Exception e) {
                    Path filePath = Path.of("D:\\OneDrive\\Studium\\HTWG\\06_SS23\\VSYS\\Tasks\\Aufgabe_2.1\\src\\pages\\404.html");

                    String content = Files.readString(filePath);
                    objectOutputStream.writeObject(content);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
