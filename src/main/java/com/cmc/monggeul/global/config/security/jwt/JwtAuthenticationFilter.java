package com.cmc.monggeul.global.config.security.jwt;

import com.cmc.monggeul.global.config.error.BaseResponse;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.error.exception.JwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.jar.JarException;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// 시큐리티 필터에서 예외는 @ExceptionHandler로 잡히지 않는다.
// Spring 이전에 filter처리가 되므로 DispatcherServlet까지 않는다!


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) { //이 필터 안걸치는 path
        String path = request.getServletPath();
        request.getMethod();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return (
                (pathMatcher.match("/user/test/kakao", path) && request.getMethod().equals("GET")) ||
                        (pathMatcher.match("/user/kakao/login", path) && request.getMethod().equals("POST")) ||
                        (pathMatcher.match("/user/test/kakao/login",path) &&request.getMethod().equals("GET")||
                                (pathMatcher.match("/user/test/google",path) &&request.getMethod().equals("GET")||
                                        pathMatcher.match("/user/test/google/code",path) &&request.getMethod().equals("GET")||
                                        pathMatcher.match("/login/oauth2/code/google",path) &&request.getMethod().equals("GET")||
                                        pathMatcher.match("/user/test/google/access",path)&&request.getMethod().equals("GET")||
                                        pathMatcher.match("/user/google/login",path)&&request.getMethod().equals("POST")||
                                        pathMatcher.match("/swagger-ui/**", path) ||
                        pathMatcher.match("/favicon.ico", path) ||
                        pathMatcher.match("/swagger-resources/**", path) ||
                        pathMatcher.match("/v3/api-docs", path)
        )));
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        ErrorCode errorCode=null;
        String jwt = getJwtFromRequest(request); //request에서 jwt 토큰을 꺼낸다.

        if(jwt==null){
            errorCode=ErrorCode.TOKEN_NOT_EXIST;
            sendErrorResponse(response,ErrorCode.TOKEN_NOT_EXIST);
        }
        else  {
            try{
                String userEmail = JwtTokenProvider.getUserEmailFromJWT(jwt); //jwt에서 사용자 id를 꺼낸다.

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEmail, null, null); //id를 인증한다.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //기본적으로 제공한 details 세팅
                SecurityContextHolder.getContext().setAuthentication(authentication); //세션에서 계속 사용하기 위해 securityContext에 Authentication 등록
                filterChain.doFilter(request, response);

                // catch 구문 jwt 토큰 유효성 검사
            }catch (IllegalArgumentException e) {
                log.error("an error occured during getting username from token", e);
                // JwtException (custom exception) 예외 발생시키기
                sendErrorResponse(response,ErrorCode.INVALID_TOKEN);
            } catch (ExpiredJwtException e) {
                log.warn("the token is expired and not valid anymore", e);
                sendErrorResponse(response,ErrorCode.ACCESS_TOKEN_EXPIRED);

            } catch(SignatureException e){
                log.error("Authentication Failed. Username or Password not valid.");
                sendErrorResponse(response,ErrorCode.FAIL_AUTHENTICATION);
            }catch(UnsupportedJwtException e){
                log.error("UnsupportedJwt");
                sendErrorResponse(response,ErrorCode.FAIL_AUTHENTICATION);
            }
        }




    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse httpServletResponse,ErrorCode errorCode) throws IOException{


        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(APPLICATION_JSON_VALUE);

        BaseResponse errorResponse = new BaseResponse(errorCode);
        //object를 텍스트 형태의 JSON으로 변환
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorResponse);
    }



}
