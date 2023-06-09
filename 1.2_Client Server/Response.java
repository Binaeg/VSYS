public class Response {

    boolean isPrime;

    long processingTime;

    long waitingTime;

    public Response(boolean isPrime, long processingTime, long waitingTime) {
        this.isPrime = isPrime;
        this.processingTime = processingTime;
        this.waitingTime = waitingTime;
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
}
