package Task01;

public class MyThreadTwo extends Thread {

    private static final int threadMax = 5;
    private static int runCount = 0;

    public static void main(String[] args) {
        for (int i = 0; i < threadMax; i++) {
            new MyThreadTwo().start();
        }
    }

    public void run() {
        while (runCount++ < 100) {
            extracted();
        }
    }

    private synchronized void extracted() {
        int random = (int) (Math.random() * 10);

        if (random > 6) {
            MyAccount.setSomeValue(MyAccount.getSomeValue() + 1);
            System.out.println(Thread.currentThread().getName() + ": Increment, new Res: " + MyAccount.getSomeValue());
        } else {
            MyAccount.setSomeValue(MyAccount.getSomeValue() - 1);
            System.out.println(Thread.currentThread().getName() + ": Decrement, new Res: " + MyAccount.getSomeValue());
        }
    }
}
