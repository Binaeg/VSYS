import java.io.IOException;

public class ClientMain {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 9090;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final boolean REQUESTMODE = true;

    private static final boolean MULTI_THREADING = true;

    public static void main(String[] args) {
        for (int i = 1; i < 2; i++) {
            PrimeClient primeClient = new PrimeClient("localhost", 9090, 9000 + i, INITIAL_VALUE, COUNT, REQUESTMODE, MULTI_THREADING);
            primeClient.start();
        }
    }

}
