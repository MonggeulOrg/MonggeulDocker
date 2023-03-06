package com.cmc.monggeul.domain.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cmc.monggeul.domain.user.dto.*;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.Role;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.FamilyRepository;
import com.cmc.monggeul.domain.user.repository.RoleRepository;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.oauth.google.GoogleOAuth;
import com.cmc.monggeul.global.config.security.SecurityConfig;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import com.cmc.monggeul.global.config.security.jwt.TokenDto;
import com.cmc.monggeul.global.config.security.userDetails.CustomUserDetailService;
import com.cmc.monggeul.global.util.AES256;
import com.cmc.monggeul.global.util.GenerateCertNumber;
import io.lettuce.core.models.role.RedisInstance;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FamilyRepository familyRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    private final CustomUserDetailService customUserDetailService;

    private final GenerateCertNumber generateCertNumber;

    private final GoogleOAuth googleOAuth;




    public PostLoginRes kakaoLogin(PostKakaoLoginReq postKakaoLoginReq, KakaoUserDto kakaoUserDto) throws BaseException {

        // 신규 가입 유저일경우
        if(userRepository.findByEmail(kakaoUserDto.getEmail()).isEmpty()){

            String matchCode=generateCertNumber.excuteGenerate();

            while(true){
                Optional<User>user=userRepository.findByMatchingCode(matchCode);
                if(user.isPresent()){
                    matchCode=generateCertNumber.excuteGenerate();
                }else{
                    break;
                }

            }

            // 유저 role 저장
            Role role=roleRepository.findByRoleCode(postKakaoLoginReq.getRole());



            User user=User.builder()
                            .name(postKakaoLoginReq.getUserName())
                            .email(kakaoUserDto.getEmail())
                            .role(role)
                            .age(postKakaoLoginReq.getAge())
                            .profileImgUrl(kakaoUserDto.getProfileImgUrl())
                            .oAuthType(User.OAuthType.KAKAO)
                            .matchingCode(matchCode)
                            .build();

            userRepository.save(user);

        }

        Authentication authentication=new UsernamePasswordAuthenticationToken(kakaoUserDto.getEmail(),null,null);
        TokenDto tokenDto= JwtTokenProvider.generateToken(authentication);
        Optional<User> user=userRepository.findByEmail(kakaoUserDto.getEmail());

        // redis에 refresh token 저장
        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        // refresh token을 복호화한 후 만료기한을 redis에 저장할 것
        DecodedJWT decodedJWT= JWT.decode(tokenDto.getRefreshToken());
        System.out.println(decodedJWT.getToken().toString());

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);

        return PostLoginRes.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .code(user.get().getMatchingCode())
                .build();

    }
    public PostLoginRes googleLogin(PostGoogleLoginReq postGoogleLoginReq, GoogleUserDto googleUserDto) throws BaseException {

        // 신규 가입 유저일경우
        if(userRepository.findByEmail(googleUserDto.getEmail()).isEmpty()){

            String matchCode=generateCertNumber.excuteGenerate();

            while(true){
                Optional<User>user=userRepository.findByMatchingCode(matchCode);
                if(user.isPresent()){
                    matchCode=generateCertNumber.excuteGenerate();
                }else{
                    break;
                }

            }

            // 유저 role 저장
            Role role=roleRepository.findByRoleCode(postGoogleLoginReq.getRole());



            User user=User.builder()
                    .name(postGoogleLoginReq.getUserName())
                    .email(googleUserDto.getEmail())
                    .role(role)
                    .age(postGoogleLoginReq.getAge())
                    .profileImgUrl(googleUserDto.getProfileImgUrl())
                    .oAuthType(User.OAuthType.GOOGLE)
                    .matchingCode(matchCode)
                    .build();

            userRepository.save(user);

        }

        Authentication authentication=new UsernamePasswordAuthenticationToken(googleUserDto.getEmail(),null,null);
        TokenDto tokenDto= JwtTokenProvider.generateToken(authentication);
        Optional<User> user=userRepository.findByEmail(googleUserDto.getEmail());

        // redis에 refresh token 저장
        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        // refresh token을 복호화한 후 만료기한을 redis에 저장할 것
        DecodedJWT decodedJWT= JWT.decode(tokenDto.getRefreshToken());
        System.out.println(decodedJWT.getToken().toString());

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);

        return PostLoginRes.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .code(user.get().getMatchingCode())
                .build();

    }

    public PostLoginRes appleLogin(PostAppleLoginReq postAppleLoginReq) throws BaseException {

        // 신규 가입 유저일경우
        Optional<User> userFind=userRepository.findByEmail(postAppleLoginReq.getAppleEmail());


        if(userFind.isEmpty()){

            String matchCode=generateCertNumber.excuteGenerate();

            while(true){
                Optional<User>userMatching=userRepository.findByMatchingCode(matchCode);
                if(userMatching.isPresent()){
                    matchCode=generateCertNumber.excuteGenerate();
                }else{
                    break;
                }

            }

            // 유저 role 저장
            Role role=roleRepository.findByRoleCode(postAppleLoginReq.getRole());



            User userSave=User.builder()
                    .name(postAppleLoginReq.getUserName())
                    .email(postAppleLoginReq.getAppleEmail())
                    .role(role)
                    .age(postAppleLoginReq.getAge())
                    .profileImgUrl(postAppleLoginReq.getProfileImgUrl())
                    .oAuthType(User.OAuthType.APPLE)
                    .matchingCode(matchCode)
                    .build();

            userRepository.save(userSave);

        }else if(userFind.get().getOAuthType()!= User.OAuthType.APPLE){
            throw new BaseException(ErrorCode.EMAIL_ALREADY_EXIST);
        }



        Authentication authentication=new UsernamePasswordAuthenticationToken(postAppleLoginReq.getAppleEmail(),null,null);
        TokenDto tokenDto= JwtTokenProvider.generateToken(authentication);
        Optional<User> user=userRepository.findByEmail(postAppleLoginReq.getAppleEmail());

        // redis에 refresh token 저장
        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        // refresh token을 복호화한 후 만료기한을 redis에 저장할 것
        DecodedJWT decodedJWT= JWT.decode(tokenDto.getRefreshToken());
        System.out.println(decodedJWT.getToken().toString());

        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);

        return PostLoginRes.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .code(user.get().getMatchingCode())
                .build();

    }

    public PostMatchingRes matching(String matchingUserCode,String userEmail){

        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(userEmail).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_EXIST)));
        Optional<User> matchingUser= Optional.ofNullable(userRepository.findByMatchingCode(matchingUserCode).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_EXIST)));

        System.out.println(user.get().getRole().getRoleCode());

        PostMatchingRes postMatchingRes = null;
        if(user.get().getRole().getRoleCode().equals("MOM")||user.get().getRole().getRoleCode().equals("DAD")){
            // 이미 매칭이 됐을 경우
            Long familyId=0L;
            Family family=null;

            if(familyRepository.findByParent(user).getId()==(familyRepository.findByChild(matchingUser).getId())){
                family=familyRepository.findByParent(user);

            }else{
                family=familyRepository.save(Family.builder()
                        .child(matchingUser.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .parent(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .build());

            }

            familyId=family.getId();
            postMatchingRes= PostMatchingRes.builder()
                    .matchingUserName(family.getChild().getName())
                    .familyId(familyId)
                    .matchingUserProfileImg(matchingUser.orElseThrow(()->new RuntimeException("프로필 이미지가 존재하지 않습니다.")).getProfileImgUrl())
                    .userProfileImg(user.orElseThrow(()->new RuntimeException("프로필 이미지가 존재하지 않습니다.")).getProfileImgUrl())
                    .build();




        }else if(user.get().getRole().getRoleCode().equals("SON")||user.get().getRole().getRoleCode().equals("DAUGHTER")){
            Long familyId=0L;
            Family family=null;


            System.out.println(familyRepository.findByChild(user).getId());
            System.out.println(familyRepository.findByParent(matchingUser).getId());
            // 이미 매칭이 됐을 경우
            if(Objects.equals(familyRepository.findByChild(user).getId(), familyRepository.findByParent(matchingUser).getId())){
                family=familyRepository.findByChild(user);

            }else{
                family=familyRepository.save(Family.builder()
                        .child(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .parent(matchingUser.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .build());

            }


            familyId=family.getId();
            postMatchingRes= PostMatchingRes.builder()
                    .matchingUserName(family.getParent().getName())
                    .matchingUserProfileImg(matchingUser.orElseThrow(()->new RuntimeException("프로필 이미지가 존재하지 않습니다.")).getProfileImgUrl())
                    .userProfileImg(user.orElseThrow(()->new RuntimeException("프로필 이미지가 존재하지 않습니다.")).getProfileImgUrl())
                    .familyId(familyId)
                    .build();

        }
        
        return postMatchingRes;



    }

    public GetUserMatchingCodeRes getMatchingCode(String userEmail){
        Optional<User> user=userRepository.findByEmail(userEmail);
        Long familyId=0L;
        if(user.get().getRole().getRoleCode().equals("MOM")||user.get().getRole().getRoleCode().equals("DAD")){

            Family family=familyRepository.findByParent(user);
            familyId=family.getId();
        }else{
            Family family=familyRepository.findByChild(user);
            familyId=family.getId();
        }

        String matchingCode=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getMatchingCode();

        return GetUserMatchingCodeRes.builder()
                .matchingCode(matchingCode)
                .familyId(familyId)
                .build();
    }

    public GetUserInfoByMatchingCodeRes getUserInfoByMatchingCode(String matchingCode){
        Optional<User>user=userRepository.findByMatchingCode(matchingCode);
        return GetUserInfoByMatchingCodeRes.builder()
                .name(user.get().getName())
                .email(user.get().getEmail())
                .build();

    }



}
