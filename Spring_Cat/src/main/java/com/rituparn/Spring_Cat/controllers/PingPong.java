package com.rituparn.Spring_Cat.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class PingPong {
    @GetMapping("/")
    public String ping() {
        return "pong";
    }
}
