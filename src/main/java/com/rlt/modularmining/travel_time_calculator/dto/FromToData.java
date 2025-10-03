package com.rlt.modularmining.travel_time_calculator.dto;

public class FromToData {
    private String from;
    private String to;

    public FromToData(String from, String to) {
        this.from = from;
        this.to = to;
    }

    // Getters and Setters
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
