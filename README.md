# Routing Station Simulation

Course: CNT 4714 - Enterprise Computing
Assignment: Project 2
Date: June 2025

## Description

A multi-threaded Java simulation of a package routing facility. Routing stations run as concurrent threads and move package groups between shared conveyor belts. The simulation uses `ReentrantLock` to synchronize access to shared conveyors and a lock-ordering strategy to prevent deadlock.

## How It Works

- Each routing station is assigned an input conveyor and an output conveyor
- Stations run concurrently using an `ExecutorService` thread pool
- To move a package group, a station must hold locks on both its input and output conveyors simultaneously
- Deadlock is prevented by always acquiring locks in index order (lower index first). If the second lock cannot be acquired, the first is released and the station backs off before retrying.
- All output is redirected to `output/simulation_output.txt`

## Technologies Used

- Java
- `java.util.concurrent` (ExecutorService, ReentrantLock)

## Project Structure

```
routing-simulation/
├── src/      Main.java, RoutingStation.java
├── config/   Configuration files for different simulation scenarios
├── output/   Sample simulation output
└── README.md
```

## How to Run

Compile and run from the project root:

```bash
javac -d bin src/*.java
java -cp bin Main
```

Output is written to `output/simulation_output.txt`.

## Switching Configurations

Edit `src/Main.java` and change the `configFile` variable on the marked line:

```
config/config.txt       3 stations (default)
config/config1.txt      5 stations
config/configSU25.txt   4 stations
```

## Config File Format

```
<number of stations>
<workload for station 0>
<workload for station 1>
...
```

Each workload value is the number of package groups that station must move before it goes offline.
