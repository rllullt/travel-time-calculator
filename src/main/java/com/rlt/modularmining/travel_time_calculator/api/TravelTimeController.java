package com.rlt.modularmining.travel_time_calculator.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TravelTimeController {

    // @GetMapping("/methodName")
    // public String getMethodName(@RequestParam String param) {
    //     return new String();
    // }
    
    @GetMapping("/hello")
    public String getHello() {
        return "Hello, World! From TravelTimeController Spring Boot Application";
    }

}
