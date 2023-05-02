package BasicServer;

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

			Message messageThread = new Message(client);
			messageThread.start();
			if (reader.readLine().equals("exit")) {
				messageThread.stop();
				System.out.println(client.sendAndReceive("exit"));
				client.disconnect();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
