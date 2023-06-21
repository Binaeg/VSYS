public interface BasicClient {
    void connect(int port, String hostname);
    void sendMessage(String message);
    String receiveMessage();
}
