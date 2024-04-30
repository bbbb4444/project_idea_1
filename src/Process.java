class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int priority;
    int ioRequestTime;
    int ioDuration;

    int ioDurationLeft;
    int ioWaitingTime;
    int runningTime;
    int readyQueueTime;

    // Total
    int waitingTime;
    int turnaroundTime;

    public Process(int pid, int arrivalTime, int burstTime, int priority, int ioRequestTime, int ioDuration) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.ioRequestTime = ioRequestTime;
        this.ioDuration = ioDuration;
        this.ioDurationLeft = ioDuration;
    }

    // Reset between algorithms
    public void reset() {
        this.ioDurationLeft = ioDuration;
        this.ioWaitingTime = 0;
        this.runningTime = 0;
        this.readyQueueTime = 0;

        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }

    @Override
    public String toString() {
        return pid + "\t" + arrivalTime + "\t\t\t\t" + burstTime + "\t\t\t" + priority + "\t\t\t" + waitingTime + "\t\t\t\t" + turnaroundTime + "\t\t\t\t\t" + readyQueueTime;
    }
}