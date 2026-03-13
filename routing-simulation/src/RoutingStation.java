/*
Name: Ethan Derby
Course: CNT 4714 Summer 2025
Assignment title: Project 2 – Multi-threaded programming in Java
Date: June 15, 2025
Class: RoutingStation.java
*/

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class RoutingStation implements Runnable {
    private int id;
    private int inputConveyor;
    private int outputConveyor;
    private int workload;
    private ReentrantLock[] conveyorLocks;
    private Random random = new Random();

    public RoutingStation(int id, int input, int output, int workload, ReentrantLock[] locks) {
        this.id = id;
        this.inputConveyor = input;
        this.outputConveyor = output;
        this.workload = workload;
        this.conveyorLocks = locks;

        System.out.println("Routing Station S" + id + ": Input conveyor assigned to conveyor number C" + inputConveyor + ".");
        System.out.println("Routing Station S" + id + ": Output conveyor assigned to conveyor number C" + outputConveyor + ".");
        System.out.println("Routing Station S" + id + " Has Total Workload of " + workload + " Package Groups.");
    }

    @Override
    public void run() {
        while (workload > 0) {
            int first = Math.min(inputConveyor, outputConveyor);
            int second = Math.max(inputConveyor, outputConveyor);

            boolean lockedFirst = conveyorLocks[first].tryLock();
            if (!lockedFirst) {
                sleepRandom();
                continue;
            }

            try {
                boolean lockedSecond = conveyorLocks[second].tryLock();
                if (!lockedSecond) {
                    System.out.println("Routing Station S" + id + ": UNABLE TO LOCK OUTPUT CONVEYOR C" + outputConveyor +
                            ". SYNCHRONIZATION ISSUE: Another station holds the lock – Station S" + id +
                            " releasing lock on input conveyor C" + inputConveyor + ".");
                    conveyorLocks[first].unlock(); 
                    continue;
                }

                try {
                    System.out.println("Routing Station S" + id + ": Currently holds lock on input conveyor C" + inputConveyor + ".");
                    System.out.println("Routing Station S" + id + ": Currently holds lock on output conveyor C" + outputConveyor + ".");
                    System.out.println("* * Routing Station S" + id + ": * * CURRENTLY HARD AT WORK MOVING PACKAGES. * *");

                    sleepRandom(); 

                    workload--;
                    System.out.println("Routing Station S" + id + ": Package group completed - " + workload + " package groups remaining to move.");
                } finally {
                    conveyorLocks[second].unlock();
                    System.out.println("Routing Station S" + id + ": Unlocks/releases output conveyor C" + outputConveyor + ".");
                }
            } finally {
                conveyorLocks[first].unlock();
                System.out.println("Routing Station S" + id + ": Unlocks/releases input conveyor C" + inputConveyor + ".");
            }

            if (workload == 0) {
                System.out.println("# # Routing Station S" + id + ": going offline – work completed! BYE! # #");
            } else {
                sleepRandom(); 
            }
        }
    }

    private void sleepRandom() {
        try {
            Thread.sleep(500 + random.nextInt(1000)); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
