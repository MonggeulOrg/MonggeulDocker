package com.cmc.monggeul.global.config.oauth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    private final GoogleOAuth googleOauth;
    private final HttpServletResponse response;

    public void request() throws IOException {
        String redirectURL=googleOauth.getOauthRedirectURL();

        response.sendRedirect(redirectURL);
    }

    public GoogleOAuthToken oAuthLogin(String code) throws IOException, ParseException, org.json.simple.parser.ParseException {



        ResponseEntity<String> accessTokenResponse= googleOauth.requestAccessToken(code);
        //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
        GoogleOAuthToken oAuthToken=googleOauth.getAccessToken(accessTokenResponse);



        return oAuthToken;

    }


}
