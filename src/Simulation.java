
import java.io.*;
import java.util.*;

public class Simulation {



    public static void main(String[] args) {

        List<Process> processes = readInputFile("src/input.txt");

        System.out.println("\nRound-Robin Scheduling:");
        roundRobin(processes, 2);

        System.out.println("\nFirst-Come First-Served (FCFS) Scheduling:");
        FCFS(processes);

        System.out.println("\nPriority Scheduling:");
        priority(processes);
    }


    private static List<Process> readInputFile(String fileName) {
        List<Process> processes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                int processId = Integer.parseInt(data[0]);
                int arrivalTime = Integer.parseInt(data[1]);
                int burstTime = Integer.parseInt(data[2]);
                int priority = Integer.parseInt(data[3]);
                int ioRequestTime = Integer.parseInt(data[4]);
                int ioDuration = Integer.parseInt(data[5]);
                processes.add(new Process(processId, arrivalTime, burstTime, priority, ioRequestTime, ioDuration));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processes;
    }


    public static void roundRobin(List<Process> processList, int quantum) {
        int fullSize = processList.size();
        Queue<Process> processes = new LinkedList<>(processList);
        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> IOQueue = new LinkedList<>();
        List<Process> finished = new ArrayList<>();

        int currentTime = 0;

        // Initialize ready queue, for processes arriving at time = 0, if possible
        checkArrivedProcesses(currentTime, processes, readyQueue);

        // Process the processes
        while (finished.size() != fullSize) {

            while (readyQueue.isEmpty()) {
                currentTime += 1;
                processIOQueue(IOQueue, readyQueue, finished);
                processReadyQueue(readyQueue);
                checkArrivedProcesses(currentTime, processes, readyQueue);
            }

            int remainingQuantum = quantum;
            Process current = readyQueue.poll();

            // Run the current process
            while (remainingQuantum > 0 && current.remainingTime > 0) {
                // Check for IO Wait request, put current process into wait list and continue with next process if so
                if (current.runningTime == current.ioRequestTime && current.ioDuration > 0) {
                    IOQueue.add(current);
                    break;
                }

                currentTime += 1;
                processIOQueue(IOQueue, readyQueue, finished);
                processReadyQueue(readyQueue);
                checkArrivedProcesses(currentTime, processes, readyQueue);

                current.runningTime += 1;
                current.turnaroundTime += 1;
                current.remainingTime -= 1;
                remainingQuantum -= 1;

                // Check for IO Wait request again, put current process into io queue and continue with next process if so
                if (current.runningTime == current.ioRequestTime && current.ioDuration > 0) {
                    IOQueue.add(current);
                    break;
                }
            }

            if (IOQueue.contains(current)) {
                continue;
            }
            // Add finished processes to the finished list. Add unfinished processes back to the ready queue
            if (current.remainingTime > 0) {
                readyQueue.offer(current);
            } else {
                current.waitingTime = current.turnaroundTime - current.burstTime;
                finished.add(current);
            }
        }
        printResults(finished);
    }

    // Helper function to put all processes that have arrived into the ready queue
    private static void checkArrivedProcesses(int currentTime, Queue<Process> processes, Queue<Process> readyQueue) {
        while (processes.peek() != null && processes.peek().arrivalTime == currentTime) {
            Process proc = processes.poll();
            readyQueue.offer(proc);
        }
    }

    // Helper function to increment time of io queue process
    private static void processIOQueue(Queue<Process> IOQueue, Queue<Process> readyQueue, List<Process> finished) {
        Process proc = IOQueue.peek();
        if (proc == null) return;

        proc.ioDuration -= 1;
        proc.turnaroundTime += 1;

        if (proc.ioDuration <= 0) {
            IOQueue.poll();
            if (proc.runningTime < proc.burstTime) {
                readyQueue.offer(proc);
            } else {
                finished.add(proc);
            }
        }
    }

    // Helper function to increment time of readyqueue processes
    private static void processReadyQueue(Queue<Process> readyQueue) {
        for (Process proc : readyQueue) {
            proc.turnaroundTime += 1;
            proc.readyQueueTime += 1;
        }
    }



    private static void priority(List<Process> processList) {
        int fullSize = processList.size();
        Queue<Process> processes = new LinkedList<>(processList);
        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> IOQueue = new LinkedList<>();
        List<Process> finished = new ArrayList<>();

        int currentTime = 0;

        // Initialize ready queue, for processes arriving at time = 0, if possible
        checkArrivedProcesses(currentTime, processes, readyQueue);

        // Process the processes
        while (finished.size() != fullSize) {

            while (readyQueue.isEmpty()) {
                currentTime += 1;
                processIOQueue(IOQueue, readyQueue, finished);
                processReadyQueue(readyQueue);
                checkArrivedProcesses(currentTime, processes, readyQueue);
            }


        }
    }



    private static void FCFS(List<Process> processList) {
        int fullSize = processList.size();
        Queue<Process> processes = new LinkedList<>(processList);
        Queue<Process> readyQueue = new LinkedList<>();
        Queue<Process> IOQueue = new LinkedList<>();
        List<Process> finished = new ArrayList<>();

        int currentTime = 0;

        // Initialize ready queue, for processes arriving at time = 0, if possible
        checkArrivedProcesses(currentTime, processes, readyQueue);

        // Process the processes
        while (finished.size() != fullSize) {

            while (readyQueue.isEmpty()) {
                currentTime += 1;
                processIOQueue(IOQueue, readyQueue, finished);
                processReadyQueue(readyQueue);
                checkArrivedProcesses(currentTime, processes, readyQueue);
            }


        }
    }



    public static void printResults(List<Process> processes) {
        System.out.println("PID\tArrival Time\tBurst Time\tPriority\tWaiting Time\tTurnaround Time\t\tRQ Time");
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        for (Process process : processes) {
            System.out.println(process);
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }

        System.out.println("Average Waiting Time: " + totalWaitingTime/ (float) processes.size());
        System.out.println("Average Turnaround Time: " + totalTurnaroundTime/ (float) processes.size());
    }
}
