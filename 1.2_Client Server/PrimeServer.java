import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import rm.requestResponse.*;

public class PrimeServer {
	private final static int PORT=9090;
	private final static Logger LOGGER=Logger.getLogger(PrimeServer.class.getName());

	private Component communication;
	private static List<Component> communicationList = new ArrayList<>();
	private int port=PORT;

	public static int threadCounter = 0;
	public static int maxThread = 8;
	public static boolean useUnlimitedThreadPool = true;
	private int communicationCounter;

	PrimeServer(int port) {
		communication=new Component();
		if(port>0) this.port=port;
		communicationList.add(communication);

		communicationCounter = threadCounter;

//    	setLogLevel(Level.FINER);
    }

	void setLogLevel(Level level) {
		for(Handler h : LOGGER.getLogger("").getHandlers()) h.setLevel(level);
		LOGGER.setLevel(level);
		LOGGER.info("Log level set to "+level);
    }

    void listen() {
		LOGGER.info("Listening on port "+port);

		while (true) {
			Long request=null;

			LOGGER.finer("Receiving ...");
			try {
				var temp = communication.receive(port, true, false).getContent();
				System.out.println(temp);
				if (!(temp instanceof Long)) {
					continue;
				}
				request = (Long) temp;
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			LOGGER.fine(request.toString()+" received.");

			LOGGER.finer("Sending ...");
//		    try {
			System.out.println("Current amount of threads: " + threadCounter++);
			if (useUnlimitedThreadPool) {
				PrimeServiceThread serviceThread = new PrimeServiceThread();
				serviceThread.setPort(port);
				serviceThread.setCommunication(communicationList.get(communicationCounter));
				serviceThread.setNumber(request);
				serviceThread.start();
			} else {
				while (true) {
					if (threadCounter < maxThread) {
						PrimeServiceThread serviceThread = new PrimeServiceThread();
						serviceThread.setPort(port);
						serviceThread.setCommunication(communication);
						serviceThread.setNumber(request);
						serviceThread.start();
						break;
					}
				}
			}
//				communication.send(new Message("localhost",port,
//		    			new Boolean(primeService(request.longValue()))),true);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			LOGGER.fine("message sent.");
		}
    }

    public static void main(String[] args) {
		int port=0;

		for (int i = 0; i<args.length; i++)  {
			switch(args[i]) {
				case "-port":
					try {
						port = Integer.parseInt(args[++i]);
					} catch (NumberFormatException e) {
						LOGGER.severe("port must be an integer, not "+args[i]);
						System.exit(1);
					}
					break;
				default:
					LOGGER.warning("Wrong parameter passed ... '"+args[i]+"'");
			}
        }


		new PrimeServer(port).listen();
    }
}
