package com.rlt.modularmining.travel_time_calculator.dto;

import java.util.List;

public class FromToResponse {
    private List<String> ruta;
    private int tiempoTotal;

    public FromToResponse(List<String> ruta, int tiempoTotal) {
        this.ruta = ruta;
        this.tiempoTotal = tiempoTotal;
    }

    public List<String> getRuta() {
        return ruta;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public void setRuta(List<String> ruta) {
        this.ruta = ruta;
    }

    public void setTiempoTotal(int tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }
}
