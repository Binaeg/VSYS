public class ServerListener implements BasicListener {
    private BasicServer server;

    public ServerListener(BasicServer server) {
        this.server = server;
    }

    @Override
    public void connectionAccepted(BasicConnection connection) {
        long connectionId = connection.getId();
        System.out.println(connectionId+ ": Connection accepted.");
        server.setListener(new ServerListener(server));
        System.out.println(connectionId + ": Waiting for message ...");
        String string = connection.receiveMessage();
        System.out.println(connectionId + ": Message received '" + string + "'");
        connection.sendMessage("server received " + string);
        System.out.println(connectionId + ": Message send.");

    }
}
