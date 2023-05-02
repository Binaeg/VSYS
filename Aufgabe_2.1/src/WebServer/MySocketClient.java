package WebServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
 
public class MySocketClient {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	
	MySocketClient(String hostname, int port) 
					throws IOException {
		socket=new Socket();
		System.out.print("Client: connecting '"+hostname+
			"' on "+port+" ... ");
		socket.connect(new InetSocketAddress(hostname,port));
		System.out.println("done.");
		objectInputStream=
			new ObjectInputStream(socket.getInputStream());
		objectOutputStream=
			new ObjectOutputStream(socket.getOutputStream());
	}

	public String sendAndReceive(String message) 
					throws Exception {
		objectOutputStream.writeObject(message);
		System.out.println("Client: send "+message);
		return "Client: received '"
			+(String)objectInputStream.readObject()+"'";
	}
	
	public void disconnect() 
					throws IOException {
		try {
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
