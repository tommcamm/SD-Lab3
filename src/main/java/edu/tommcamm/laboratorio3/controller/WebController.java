package edu.tommcamm.laboratorio3.controller;

import edu.tommcamm.laboratorio3.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class WebController {

    @GetMapping("/manualinput")
    public String manualInput() {
        return "input";
    }

    @GetMapping("/")
    public String index(@RequestHeader (value = "User-Agent") String userAgent) {
        if (!userAgent.contains("Gecko") && !userAgent.contains("Webkit"))
            throw new ResourceNotFoundException();
        return "index";
    }
}
