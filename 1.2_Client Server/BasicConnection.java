public interface BasicConnection {
    String receiveMessage();
    void sendMessage(String message);
    void close();
    long getId();
}
