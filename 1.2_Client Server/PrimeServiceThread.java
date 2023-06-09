import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

public class PrimeServiceThread extends Thread{

    private long number;

    private CounterThread threadCounter;

    private int sendPort;

    private long startWaitingTime;

    @Override
    public void run() {
        Response response = new Response();

        long startProcessingTime = System.currentTimeMillis();
        boolean isPrime = primeService(number);
        long endProcessingTime = System.currentTimeMillis();


        response.setPrime(isPrime);
        response.setProcessingTime(endProcessingTime-startProcessingTime);
        response.setWaitingTime(startProcessingTime-startWaitingTime);
        Message message = new Message("localhost", sendPort, response);
        Component communication = new Component();
        try {
            communication.send(message, sendPort, true);
            threadCounter.decThreadCounter();
//            System.out.println("Current amount of threads: " + threadCounter.getThreadCounter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number)+1; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public void setThreadCounter(CounterThread threadCounter) {
        this.threadCounter = threadCounter;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    public void setStartWaitingTime(long startWaitingTime) {
        this.startWaitingTime = startWaitingTime;
    }
}
