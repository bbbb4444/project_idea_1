
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
        List<Process> ioWait = new ArrayList<>();
        List<Process> finished = new ArrayList<>();

        int currentTime = 0;

        // Initialize ready queue, for processes arriving at time = 0, if possible
        checkArrivedProcesses(currentTime, processes, readyQueue);

        // Process ready queue
        while (finished.size() != fullSize) {

            while (readyQueue.isEmpty()) {
                currentTime += 1;
                processWaitList(ioWait, readyQueue);
                processReadyQueue(readyQueue);
                checkArrivedProcesses(currentTime, processes, readyQueue);
            }

            int remainingQuantum = quantum;
            Process current = readyQueue.poll();

            // Run the current process
            while (remainingQuantum > 0 && current.remainingTime > 0) {
                // Check for IO Wait request, put current process into wait list and continue with next process if so
                if (current.runningTime == current.ioRequestTime && current.runningTime < current.ioDuration) {
                    ioWait.add(current);
                    break;
                }

                currentTime += 1;
                processWaitList(ioWait, readyQueue);
                processReadyQueue(readyQueue);
                checkArrivedProcesses(currentTime, processes, readyQueue);

                current.runningTime += 1;
                current.turnaroundTime += 1;
                current.remainingTime -= 1;
                remainingQuantum -= 1;
            }

            if (ioWait.contains(current)) {
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
            System.out.println("e");
        }
    }

    // Helper function to increment time of wait list processes
    private static void processWaitList(List<Process> waitList, Queue<Process> readyQueue) {
        Iterator<Process> iterator = waitList.iterator();
        while (iterator.hasNext()) {
            Process proc = iterator.next();
            proc.ioDuration -= 1;
            proc.turnaroundTime += 1;
            System.out.println("dur:" +proc.ioDuration);

            if (proc.ioDuration <= 0) {
                iterator.remove();
                readyQueue.offer(proc);
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



    private static void priority(List<Process> processes) {
    }



    private static void FCFS(List<Process> processes) {
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