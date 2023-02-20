package com.cmc.monggeul.global.config.oauth.google;

import lombok.*;


@Getter
@Setter
public class GoogleOAuthToken {
    private String access_token;

    public GoogleOAuthToken(String access_token){
        this.access_token=access_token;
    }
}