package WebServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientMain {
	private static final int port=1234;
	private static final String hostname="localhost";
	private static MySocketClient client;
	private static BufferedReader reader;

	public static void main(String args[]) {
		try {
			client=new MySocketClient(hostname,port);
			reader=new BufferedReader(new InputStreamReader(System.in ));
			while (true) {
				System.out.print("Request website: ");
				String page = reader.readLine();
				System.out.println(client.sendAndReceive(page));
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
