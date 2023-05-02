package WebServer;

public class Message extends Thread {
    private int counter = 0;
    private String messageBody = "Test-";
    private MySocketClient client;

    public Message(MySocketClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
//            while (true) {
                System.out.println(client.sendAndReceive(messageBody));
//                counter++;
//                sleep((long) (1000 * (Math.random() + 1)));
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public MySocketClient getClient() {
        return client;
    }

    public void setClient(MySocketClient client) {
        this.client = client;
    }
}
