package com.cmc.monggeul.global.config.security;

import com.cmc.monggeul.global.config.security.jwt.JwtAuthenticationFilter;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    private JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt 사용
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/test").permitAll()
                .antMatchers("/user/kakao/login").permitAll()
                .antMatchers("/user/test/kakao").permitAll()
                .antMatchers("/user/test/kakao/login").permitAll()
                .antMatchers("/user/auth/**","/user/test").permitAll()
                .anyRequest().authenticated() // 이밖에 모든 요청은 인증이 필요
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);




        return http.build();



    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}