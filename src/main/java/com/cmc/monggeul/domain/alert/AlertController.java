package com.cmc.monggeul.domain.alert;

import com.cmc.monggeul.domain.alert.dto.GetAlertRes;
import com.cmc.monggeul.domain.alert.service.AlertService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.security.jwt.JwtAuthenticationFilter;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlertController {


    private final AlertService alertService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/alert")
    public ResponseEntity<BaseResponse<List<GetAlertRes>>>getAlert(HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        List<GetAlertRes>getAlertResList=alertService.getAlert(userEmail);
        return ResponseEntity.ok(new BaseResponse<>(getAlertResList));

    }


}
