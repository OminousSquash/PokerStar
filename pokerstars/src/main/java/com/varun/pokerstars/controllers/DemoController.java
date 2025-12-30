package com.varun.pokerstars.controllers;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DemoController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping
    public String post() {
        return "post";
    }
}
