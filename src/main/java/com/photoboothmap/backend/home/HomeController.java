package com.photoboothmap.backend.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String healthCheck(){
        return "Hello World!";
    }
}
