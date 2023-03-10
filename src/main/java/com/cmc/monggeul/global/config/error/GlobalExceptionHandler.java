package com.cmc.monggeul.global.config.error;

import com.cmc.monggeul.global.config.error.exception.BaseException;

import com.cmc.monggeul.global.config.error.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cmc.monggeul.global.config.error.ErrorCode.*;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // CustomException
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<BaseResponse> handleCustomException(JwtException e, HttpServletRequest request) {
        System.out.println("jwt error");
        return ResponseEntity
                .status(e.getJwtErrorCode().getHttpStatus())
                .body(new BaseResponse<>(e));
    }

    // Not Support Http Method Exception
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse> handleHttpMethodException(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request
    ) {
        log.error("[HttpRequestMethodNotSupportedException] " +
                        "url: {} | errorType: {} | errorMessage: {} | cause Exception: ",
                request.getRequestURL(), INVALID_HTTP_METHOD, INVALID_HTTP_METHOD.getMessage(), e);

        return ResponseEntity
                .status(INVALID_HTTP_METHOD.getHttpStatus())
                .body(new BaseResponse<>(INVALID_HTTP_METHOD));
    }

    // Validation Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> (error.getField() + ": " +
                        Objects.requireNonNullElse(error.getDefaultMessage(), "")).trim())
                .collect(Collectors.toList());

        JwtException customException = new JwtException(INVALID_VALUE, StringUtils.join(errors));
        return ResponseEntity
                .status(INVALID_VALUE.getHttpStatus())
                .body(new BaseResponse<>(customException));
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<BaseResponse> handleAuthenticationCredentialsNotFoundException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(new BaseResponse<>(ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleConstraintViolationException(ConstraintViolationException e) {
        JwtException customException = new JwtException(INVALID_VALUE, e.getMessage());
        return ResponseEntity
                .status(INVALID_VALUE.getHttpStatus())
                .body(new BaseResponse<>(customException));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(new BaseResponse<>(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(new BaseResponse<>(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseResponse<?>> handleBindException(BindException e) {
        return ResponseEntity.badRequest().body(new BaseResponse<>(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(new BaseResponse<>(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseResponse<?>>handleNoSuchElementException(NoSuchElementException e){
        return ResponseEntity.badRequest().body(new BaseResponse<Object>(BAD_REQUEST));
    }

    // Application Exception
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getCode())
                .body(new BaseResponse<>(e));
    }

    //Runtime Exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> RuntimeException(RuntimeException e, HttpServletRequest request) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // 이외 Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("[Common Exception] url: {} | errorMessage: {}",
                request.getRequestURL(), e.getMessage());
        e.printStackTrace();
        return ResponseEntity
                .status(SERVER_INTERNAL_ERROR.getHttpStatus())
                .body(new BaseResponse<>(SERVER_INTERNAL_ERROR));
    }


}