package com.github.aurinosalvador.anlixapi.services;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/ping")
public class PingService {
    @GetMapping("/")
    String getPing() {
        return "ok";
    }

}
