package com.cmc.monggeul;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<HttpStatus> healthCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/apple/oauth")
    public ResponseEntity<HttpStatus> appleOauth(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
