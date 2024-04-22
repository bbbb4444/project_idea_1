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
        List<Process> finished = new ArrayList<>();

        int currentTime = 0;

        while (finished.size() != fullSize) {

            // Put all processes that have arrived into the ready queue
            while (processes.peek() != null && processes.peek().arrivalTime <= currentTime) {
                Process proc = processes.poll();
                readyQueue.offer(proc);
            }
            int remainingQuantum = quantum;

            Process current = readyQueue.poll();

            while (remainingQuantum > 0 && current.remainingTime > 0) {
                current.remainingTime -= 1;
                remainingQuantum -= 1;
                currentTime += 1;

                // Calculate how long processes have been waiting in the ready queue
                for (Process proc : readyQueue) {
                    proc.readyQueueTime += 1;
                }
            }

            if (current.remainingTime > 0) {
                readyQueue.offer(current);
            } else {
                finished.add(current);
            }

        }
        printResults(finished);
    }

    private static void priority(List<Process> processes) {
    }

    private static void FCFS(List<Process> processes) {
    }

    public static void printResults(List<Process> processes) {
        System.out.println("PID\tArrival Time\tBurst Time\tPriority\tWaiting Time\tTurnaround Time\t\tRQ Time");
        for (Process process : processes) {
            System.out.println(process);
        }
    }
}