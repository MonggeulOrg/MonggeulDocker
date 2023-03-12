package com.cmc.monggeul.domain.alert;

import com.cmc.monggeul.domain.alert.dto.GetAlertRes;
import com.cmc.monggeul.domain.alert.dto.PostResponseAlertRes;
import com.cmc.monggeul.domain.alert.service.AlertService;
import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.security.jwt.JwtAuthenticationFilter;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlertController {


    private final AlertService alertService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(
            value ="[홈] 메세지 조회" )
    @GetMapping("/alert")
    public ResponseEntity<BaseResponse<List<GetAlertRes>>>getAlert(HttpServletRequest request){
        String jwtToken=jwtAuthenticationFilter.getJwtFromRequest(request);
        String userEmail=jwtTokenProvider.getUserEmailFromJWT(jwtToken);
        List<GetAlertRes>getAlertResList=alertService.getAlert(userEmail);
        return ResponseEntity.ok(new BaseResponse<>(getAlertResList));

    }

    @ApiOperation(
            value ="[홈] 메세지 확인" )
    @PatchMapping("/alert/read/{alertId}")
    public ResponseEntity<BaseResponse<PostResponseAlertRes>> readAlert(@PathVariable("alertId")Long alertId){
        PostResponseAlertRes postResponseAlertRes=alertService.readAlert(alertId);
        return ResponseEntity.ok(new BaseResponse<>(postResponseAlertRes));

    }


}
