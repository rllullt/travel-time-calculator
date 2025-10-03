package com.rlt.modularmining.travel_time_calculator.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.rlt.modularmining.travel_time_calculator.TravelTimeGraph;


@RestController
public class TravelTimeController {

    // @GetMapping("/methodName")
    // public String getMethodName(@RequestParam String param) {
    //     return new String();
    // }

    private TravelTimeGraph graph = new TravelTimeGraph();
    
    @GetMapping("/hello")
    public String getHello() {
        return "Hello, World! From TravelTimeController Spring Boot Application";
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {
            // Reading and processing the CSV content using OpenCSV
            List<String[]> rows = csvReader.readAll();

            // Build the graph from CSV records
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row[0].equals("")) {
                    break;
                }
                System.out.println("Path: " + String.join(",", row));
                graph.addPath(row[0]);
            }

            graph.printGraph();

            return ResponseEntity.ok("CSV file uploaded and processed successfully.");

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to process the CSV file: " + e.getMessage());
        }
    }
}
