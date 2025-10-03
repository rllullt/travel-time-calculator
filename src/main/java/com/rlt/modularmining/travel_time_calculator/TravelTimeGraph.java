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
    }

    Map<String, ArrayList<Edge>> graph;
    
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
}
