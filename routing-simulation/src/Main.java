import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Main {
    public static final int MAX_STATIONS = 10;
    public static final int NUM_CONVEYORS = 5;

    public static void main(String[] args) {
        
        try {
            new File("output").mkdirs(); 
            PrintStream out = new PrintStream(new FileOutputStream("output/simulation_output.txt"));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String configFile = "config/config.txt";  // Change to config1.txt or configSU25.txt if needed
        List<Integer> workloads = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            int numStations = Integer.parseInt(reader.readLine().trim());

            for (int i = 0; i < numStations; i++) {
                workloads.add(Integer.parseInt(reader.readLine().trim()));
            }

            ReentrantLock[] conveyorLocks = new ReentrantLock[NUM_CONVEYORS];
            for (int i = 0; i < NUM_CONVEYORS; i++) {
                conveyorLocks[i] = new ReentrantLock();
            }

            ExecutorService executor = Executors.newFixedThreadPool(MAX_STATIONS);

            for (int i = 0; i < workloads.size(); i++) {
                int input = i;
                int output = (i + 1) % NUM_CONVEYORS;
                RoutingStation station = new RoutingStation(i, input, output, workloads.get(i), conveyorLocks);
                executor.execute(station);
            }

            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
