package com.rituparn.Spring_Cat.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class PingPong {
    @GetMapping("/ping")
    public String pingPong() {
        return "pingpong";
    }
}
