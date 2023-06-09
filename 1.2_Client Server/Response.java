import java.io.Serializable;

public class Response implements Serializable {

    boolean isPrime;

    long processingTime;

    long waitingTime;

    public Response(boolean isPrime, long processingTime, long waitingTime) {
        this.isPrime = isPrime;
        this.processingTime = processingTime;
        this.waitingTime = waitingTime;
    }

    public Response() {
    }

    public boolean isPrime() {
        return isPrime;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public void setPrime(boolean prime) {
        isPrime = prime;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }
}
