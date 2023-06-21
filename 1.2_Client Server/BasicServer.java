public interface BasicServer {
    void waitForConnection(int port, PrimeServer listener);
    void setListener(BasicListener listener);
}
