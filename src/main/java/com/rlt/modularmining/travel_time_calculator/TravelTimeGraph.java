package com.rlt.modularmining.travel_time_calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

class Edge {
    String to;
    int weight;

    public Edge(String to, int weight) {
        this.to = to;
        this.weight = weight;
    }

    public String getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }
}

public class TravelTimeGraph {
    public TravelTimeGraph() {
        graph = new HashMap<>();
        distances = new HashMap<>();
    }

    Map<String, ArrayList<Edge>> graph;
    Map<String, HashMap<String, Path>> distances;
    
    public void addPath(String path) {
        String[] nodsAndTimes = path.split(";");
        String from = nodsAndTimes[0];
        String to = nodsAndTimes[1];
        int time = Integer.parseInt(nodsAndTimes[2]);

        // We assume every path is listed once
        if (!graph.containsKey(from)) {
            graph.put(from, new ArrayList<>());
        }
        graph.get(from).add(new Edge(to, time));
    }

    public void printGraph() {
        for (Map.Entry<String, ArrayList<Edge>> entry : graph.entrySet()) {
            String from = entry.getKey();
            ArrayList<Edge> destinations = entry.getValue();
            System.out.print(from + " -> ");
            for (Edge edge : destinations) {
                System.out.print(edge.getTo() + ": " + edge.getWeight() + "; ");
            }
            System.out.println();
        }
    }
    
    public void printDistances() {
        for (Map.Entry<String, HashMap<String, Path>> entry : distances.entrySet()) {
            String from = entry.getKey();
            HashMap<String, Path> destinations = entry.getValue();
            System.out.print(from + " -> ");
            for (Map.Entry<String, Path> destination : destinations.entrySet()) {
                String to = destination.getKey();
                Path path = destination.getValue();
                System.out.print(to + " through: [" + String.join(", ", path.getNodes()) + "] with total weight: " + path.getTotalWeight() + "; ");
            }
            System.out.println();
        }
    }

    /*
     * Find the shortest path from a given source node to all other nodes in the graph
     * using Dijkstra’s algorithm.
     */
    public static Map<String, Path> dijkstra(Map<String, ArrayList<Edge>> graph, String start) {
        PriorityQueue<Path> pq = new PriorityQueue<>(Comparator.comparingInt(Path::getTotalWeight));
        HashMap<String, Path> dist = new HashMap<>();

        // Fill every distance in dist array with infinity
        for (String node : graph.keySet()) {
            dist.put(node, new Path(node, new ArrayList<>(List.of(node)), Integer.MAX_VALUE));
        }
        dist.put(start, new Path(start, new ArrayList<>(List.of(start)), 0));
        
        pq.add(new Path(start, new ArrayList<>(List.of(start)), 0));

        // Loop until the priority queue is empty
        while (!pq.isEmpty()) {
            // Get the node with the minimum distance
            Path curr = pq.poll();
            String u = curr.getTo();
            System.out.println("Processing node: " + u);

            // Visit each neighbor of the current node
            for (Edge neighbor : graph.get(u)) {
                String v = neighbor.getTo();
                int weight = neighbor.getWeight();
                System.out.println("  Visiting neighbor " + v + " from " + u + " with edge weight " + weight);
                System.out.println("  Current dist to " + v + " is " + dist.get(v).getTotalWeight() + " through " + String.join(", ", dist.get(v).getNodes()));

                // If the path to v through u is shorter, update the distance and add to the priority queue
                if (dist.get(v).getTotalWeight() > dist.get(u).getTotalWeight() + weight) {
                    // Update distance and path to v
                    int newDistance = dist.get(u).getTotalWeight() + weight;
                    ArrayList<String> newNodes = new ArrayList<>(dist.get(u).getNodes());
                    newNodes.add(v);
                    Path newPath = new Path(v, newNodes, newDistance);
                    dist.put(v, newPath);
                    pq.add(newPath);
                    System.out.println("    Set distance to " + v + " to " + newDistance + " through " + String.join(", ", newNodes));
                }
            }
        }
        return dist;
    }

    public Map<String, HashMap<String, Path>> getAllDistances() {
        if (distances.isEmpty()) {
            computeAllDistances();
        }
        return distances;
    }

    private Map<String, HashMap<String, Path>> computeAllDistances() {
        distances = new HashMap<>();
        for (String node : graph.keySet()) {
            distances.put(node, new HashMap<>(dijkstra(graph, node)));
        }
        return distances;
    }
}
