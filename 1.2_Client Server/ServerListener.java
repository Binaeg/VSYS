public class ServerListener implements BasicListener {
    private BasicServer server;
    BasicConnection connection;

    public ServerListener(BasicServer server) {
        this.server = server;
    }

    String message;

    @Override
    public void connectionAccepted(BasicConnection connection) {
        this.connection = connection;
        long connectionId = connection.getId();
        System.out.println(connectionId+ ": Connection accepted.");
        server.setListener(new ServerListener(server));
        System.out.println(connectionId + ": Waiting for message ...");

    }

    public void sendMessage(String message) {
        connection.sendMessage(message);

    }

    public String receiveMessage() {
        String string = connection.receiveMessage();
        System.out.println(connection.getId() + ": Message received '" + string + "'");
        return string;
    }
}
