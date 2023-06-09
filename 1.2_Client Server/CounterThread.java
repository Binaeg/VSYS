public class CounterThread extends Thread {

    public int threadCounter;

    @Override
    public void run(){
        threadCounter = 0;
    }

    public synchronized int getThreadCounter() {
        return threadCounter;
    }

    public synchronized void incThreadCounter() {
        this.threadCounter++;
        System.out.println("Current amount of threads: " + threadCounter);
    }
    public synchronized


    public synchronized void decThreadCounter() {
        this.threadCounter--;
        System.out.println("Current amount of threads: " + threadCounter);
    }
}
