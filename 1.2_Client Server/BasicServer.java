public interface BasicServer {
    void waitForConnection(int port);
    void setListener(BasicListener listener);
}
