class Process implements Comparable<Process> {
    int pid;
    int arrivalTime;
    int burstTime;
    int priority;
    int ioRequestTime;
    int ioDuration;

    int ioDurationLeft;
    int runningTime;
    int remainingTime;
    int waitingTime;
    int turnaroundTime;
    int readyQueueTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority, int ioRequestTime, int ioDuration) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.ioRequestTime = ioRequestTime;
        this.ioDuration = ioDuration;
        this.remainingTime = burstTime;
        this.ioDurationLeft = ioDuration;
    }

    public void reset() {
        this.remainingTime = burstTime;
        this.ioDurationLeft = ioDuration;
        this.runningTime = 0;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.readyQueueTime = 0;
    }

    @Override
    public String toString() {
        return pid + "\t" + arrivalTime + "\t\t\t\t" + burstTime + "\t\t\t" + priority + "\t\t\t" + waitingTime + "\t\t\t\t" + turnaroundTime + "\t\t\t\t\t" + readyQueueTime;
    }

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.priority, other.priority);
    }
}