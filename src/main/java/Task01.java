
public class Task01 extends Thread {
    private static final int threadMax = 10;
    private static int runCount = 0;

    public synchronized void run() {
//      1a Threads werden nicht nach einer Reihenfolge abgearbeitet, Reihenfolge anders nach Neustart
        while (runCount ++ < 100) {
            schleifenrumpf();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < threadMax; i++) {
            new Task01().start();
        }
    }

//  1b Threads sperren Ressource auf die sie zugreifen, die Threads an sich werden aber nebenläufig ausgeführt
    public synchronized void schleifenrumpf() {
        System.out.println(runCount + ": " + Thread.currentThread().getName());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
