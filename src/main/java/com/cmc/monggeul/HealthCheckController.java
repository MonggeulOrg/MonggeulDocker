package com.cmc.monggeul;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<HttpStatus> healthCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/apple/oauth")
    public void appleOauth(HttpServletResponse httpServletResponse) throws IOException {
        System.out.println(httpServletResponse.toString());
        httpServletResponse.getWriter();
    }
}
