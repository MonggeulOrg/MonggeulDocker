package com.cmc.monggeul.global.config.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String JWT_SECRET = "secretKeyKDISKdjffjsfgjojroijfdggsdfswfkjfqkoqprqpoSdakwkepoqwekqpokdf";

    private long tokenValidTime = 30 * 60 * 1000L;

    // 토큰 유효시간
    private static final int JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24*14;

    // jwt 토큰 생성
    public static TokenDto generateToken(Authentication authentication) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        String accessToken=Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(new Date(now.getTime()+1000 * 60 * 60 * 24)) // 만료 시간 세팅 (1일)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        String refreshToken=Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(expiryDate) // 만료 시간 세팅
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    // Jwt 토큰에서 아이디 추출
    public static String getUserEmailFromJWT(String token) {


        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }



}