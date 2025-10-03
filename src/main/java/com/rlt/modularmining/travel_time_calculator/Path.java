package com.rlt.modularmining.travel_time_calculator;

import java.util.List;

public class Path {
    String to;
    List<String> nodes;
    int totalWeight;

    public Path(String to, List<String> nodes, int totalWeight) {
        this.to = to;
        this.nodes = nodes;
        this.totalWeight = totalWeight;
    }

    public String getTo() {
        return to;
    }
    
    public List<String> getNodes() {
        return nodes;
    }
    
    public int getTotalWeight() {
        return totalWeight;
    }
}
