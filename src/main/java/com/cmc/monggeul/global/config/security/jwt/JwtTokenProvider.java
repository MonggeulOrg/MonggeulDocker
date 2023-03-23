package com.cmc.monggeul.global.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.redis.RedisDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String JWT_SECRET = "secretKeyKDISKdjffjsfgjojroijfdggsdfswfkjfqkoqprqpoSdakwkepoqwekqpokdf";

    private long tokenValidTime = 30 * 60 * 1000L;

    private static RedisDao redisDao;

    // 토큰 유효시간
    private static final int JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24*14;

    // jwt 토큰 생성
    public static TokenDto generateToken(Authentication authentication) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        String accessToken=Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                // 1일 : 1000 * 60 * 60 * 24
                .setExpiration(new Date(now.getTime()+1000 * 60*60*24))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        String refreshToken=Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                // expiryDate
                .setExpiration(expiryDate) // 만료 시간 세팅
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();
        DecodedJWT decodedJWT= JWT.decode(refreshToken);

        //redisDao.setValues(authentication,refreshToken,Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())));

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
    public Subject getSubject(String accessToken) throws JsonProcessingException {
        String subjectStr = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(accessToken).getBody().getSubject();
        return new ObjectMapper().readValue(subjectStr, Subject.class);
    }


   // refresh token으로 accessToken 재발급

    public TokenDto reissueAtk(String userEmail,String rtkEmail) throws JsonProcessingException {

        if(!rtkEmail.equals(userEmail)){
            throw new BaseException(ErrorCode.EXPIRED_AUTHENTICATION);
        }
        Authentication authentication=new UsernamePasswordAuthenticationToken(userEmail,null,null);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        String accessToken=Jwts.builder()
                .setSubject((String) authentication.getPrincipal()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(new Date(now.getTime()+1000 * 60*60*24))
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
    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(JWT_SECRET).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }




}