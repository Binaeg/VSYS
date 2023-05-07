package WebServer;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class MySocketServerConnection extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private static String httpHeaderSuccess = "HTTP/1.1 200 OK\r\n\r\n";
    private static String httpHeaderError = "HTTP/1.1 404 NOT FOUND\r\n\r\n";

    public MySocketServerConnection(Socket socket)
            throws IOException {
        this.socket = socket;
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(isr);
        System.out.println("Server: incoming connection accepted.");
    }

    public void run() {

        try {
            String page = "/index.html";
            String line = reader.readLine();
            while (!line.isEmpty()) {
                if (line.contains("GET")) {
                    System.out.println(line);
                    page = line.substring(4, line.length() - 9);

                    Path filePath;
                    String content;
                    try {
                        filePath = Path.of("D:\\OneDrive\\Studium\\HTWG\\06_SS23\\VSYS\\Tasks\\Aufgabe_2.1\\src\\pages" + page);
                        content = httpHeaderSuccess + Files.readString(filePath);

                    } catch (Exception e) {
                        filePath = Path.of("D:\\OneDrive\\Studium\\HTWG\\06_SS23\\VSYS\\Tasks\\Aufgabe_2.1\\src\\pages\\404.html");
                        content = httpHeaderError + Files.readString(filePath);
                    }


                    socket.getOutputStream().write(content.getBytes("UTF-8"));
                    socket.close();
                }

                line = reader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
