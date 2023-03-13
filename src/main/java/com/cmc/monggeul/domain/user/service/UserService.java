package com.cmc.monggeul.domain.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cmc.monggeul.domain.diary.repository.DiaryRepository;
import com.cmc.monggeul.domain.user.dto.*;
import com.cmc.monggeul.domain.user.entity.Family;
import com.cmc.monggeul.domain.user.entity.Role;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.FamilyRepository;
import com.cmc.monggeul.domain.user.repository.RoleRepository;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.BaseEntity;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import com.cmc.monggeul.global.config.oauth.google.GoogleOAuth;
import com.cmc.monggeul.global.config.oauth.kakao.KakaoService;
import com.cmc.monggeul.global.config.redis.RedisDao;
import com.cmc.monggeul.global.config.security.SecurityConfig;
import com.cmc.monggeul.global.config.security.jwt.JwtTokenProvider;
import com.cmc.monggeul.global.config.security.jwt.TokenDto;
import com.cmc.monggeul.global.config.security.userDetails.CustomUserDetailService;
import com.cmc.monggeul.global.util.AES256;
import com.cmc.monggeul.global.util.GenerateCertNumber;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.lettuce.core.models.role.RedisInstance;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final RedisDao redisDao;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FamilyRepository familyRepository;

    private final DiaryRepository diaryRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    private final CustomUserDetailService customUserDetailService;

    private final GenerateCertNumber generateCertNumber;

    private final GoogleOAuth googleOAuth;

    private final KakaoService kakaoService;






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
        redisDao.setValues(authentication,tokenDto.getRefreshToken(),Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())));

        return PostLoginRes.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .profileImg(user.orElseThrow().getProfileImgUrl())
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
        redisDao.setValues(authentication,tokenDto.getRefreshToken(),Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())));
        return PostLoginRes.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .code(user.get().getMatchingCode())
                .profileImg(user.orElseThrow().getProfileImgUrl())
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
        redisDao.setValues(authentication,tokenDto.getRefreshToken(),Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())));
        return PostLoginRes.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .code(user.get().getMatchingCode())
                .profileImg(user.orElseThrow().getProfileImgUrl())
                .build();

    }

    public PostMatchingRes matching(String matchingUserCode,String userEmail){

        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(userEmail).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_EXIST)));
        Optional<User> matchingUser= Optional.ofNullable(userRepository.findByMatchingCode(matchingUserCode).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_EXIST)));


        PostMatchingRes postMatchingRes = null;
        System.out.println(familyRepository.findByParent(user));
        if(user.get().getRole().getRoleCode().equals("MOM")||user.get().getRole().getRoleCode().equals("DAD")){

            // 이미 매칭이 됐을 경우
            Long familyId=0L;
            Family family=null;

            if(familyRepository.findByParent(user)==null){
                family=familyRepository.save(Family.builder()
                        .child(matchingUser.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .parent(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .build());
            }else{
                family=familyRepository.findByParent(user);
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


            if(familyRepository.findByChild(user)==null){
                family=familyRepository.save(Family.builder()
                        .child(matchingUser.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .parent(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)))
                        .build());
            }else{
                // 이미 매칭이 됐을 경우
                family=familyRepository.findByChild(user);
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

    public GetUserMyPageDto getUserMyPage(String userEmail){
        Optional<User>user=userRepository.findByEmail(userEmail);
        Family family;
        GetUserMyPageDto getUserMyPageDto=null;
        int diaryCount=0;
        if(user.get().getRole().getRoleCode().equals("MOM")||user.get().getRole().getRoleCode().equals("DAD")){

            family=familyRepository.findByParent(user);
            diaryCount=diaryRepository.getDiaryCount(family.getId());

            getUserMyPageDto=GetUserMyPageDto.builder()
                    .nickname(user.orElseThrow().getName())
                    .age(user.orElseThrow().getAge())
                    .diaryCount(diaryCount)
                    .email(userEmail)
                    .authType(user.get().getOAuthType())
                    .userProfileImg(user.orElseThrow().getProfileImgUrl())
                    .userRole(user.orElseThrow().getRole().getRoleCode())
                    .communityPostCount(0)
                    .status(user.orElseThrow().getStatus())
                    .build();

        }else{
            family=familyRepository.findByChild(user);
            diaryCount=diaryRepository.getDiaryCount(family.getId());

            getUserMyPageDto=GetUserMyPageDto.builder()
                    .nickname(user.orElseThrow().getName())
                    .age(user.orElseThrow().getAge())
                    .diaryCount(diaryCount)
                    .email(userEmail)
                    .authType(user.get().getOAuthType())
                    .userProfileImg(user.orElseThrow().getProfileImgUrl())
                    .userRole(user.orElseThrow().getRole().getRoleCode())
                    .communityPostCount(0)
                    .status(user.orElseThrow().getStatus())
                    .build();

        }

        return getUserMyPageDto;

    }

    // 마이페이지 수정
    public GetUserMyPageDto patchMyPage(String userEmail,PatchUserMyPageDto patchUserMyPageDto){
        Optional<User>user=userRepository.findByEmail(userEmail);
        user.orElseThrow().updateAge(patchUserMyPageDto.getAge());
        user.orElseThrow().updateName(patchUserMyPageDto.getName());
        user.orElseThrow().updateProfileImg(patchUserMyPageDto.getProfileImg());
        Family family;
        GetUserMyPageDto getUserMyPageDto=null;
        int diaryCount=0;
        if(user.get().getRole().getRoleCode().equals("MOM")||user.get().getRole().getRoleCode().equals("DAD")){

            family=familyRepository.findByParent(user);
            diaryCount=diaryRepository.getDiaryCount(family.getId());

            getUserMyPageDto=GetUserMyPageDto.builder()
                    .nickname(user.orElseThrow().getName())
                    .age(user.orElseThrow().getAge())
                    .diaryCount(diaryCount)
                    .email(userEmail)
                    .authType(user.get().getOAuthType())
                    .userProfileImg(user.orElseThrow().getProfileImgUrl())
                    .userRole(user.orElseThrow().getRole().getRoleCode())
                    .communityPostCount(0)
                    .build();

        }else{
            family=familyRepository.findByChild(user);
            diaryCount=diaryRepository.getDiaryCount(family.getId());

            getUserMyPageDto=GetUserMyPageDto.builder()
                    .nickname(user.orElseThrow().getName())
                    .age(user.orElseThrow().getAge())
                    .diaryCount(diaryCount)
                    .email(userEmail)
                    .authType(user.get().getOAuthType())
                    .userProfileImg(user.orElseThrow().getProfileImgUrl())
                    .userRole(user.orElseThrow().getRole().getRoleCode())
                    .communityPostCount(0)
                    .build();

        }

        return getUserMyPageDto;


    }

    // 회원탈퇴하기
    public GetUserMyPageDto deleteUser(String userEmail){
        Optional<User>user=userRepository.findByEmail(userEmail);
        user.orElseThrow().updateStatus(BaseEntity.Status.DELETE);
        Family family;
        GetUserMyPageDto getUserMyPageDto=null;
        int diaryCount=0;
        if(user.get().getRole().getRoleCode().equals("MOM")||user.get().getRole().getRoleCode().equals("DAD")){

            family=familyRepository.findByParent(user);
            diaryCount=diaryRepository.getDiaryCount(family.getId());

            getUserMyPageDto=GetUserMyPageDto.builder()
                    .nickname(user.orElseThrow().getName())
                    .age(user.orElseThrow().getAge())
                    .diaryCount(diaryCount)
                    .email(userEmail)
                    .authType(user.get().getOAuthType())
                    .userProfileImg(user.orElseThrow().getProfileImgUrl())
                    .userRole(user.orElseThrow().getRole().getRoleCode())
                    .communityPostCount(0)
                    .status(user.orElseThrow().getStatus())
                    .build();

        }else{
            family=familyRepository.findByChild(user);
            diaryCount=diaryRepository.getDiaryCount(family.getId());

            getUserMyPageDto=GetUserMyPageDto.builder()
                    .nickname(user.orElseThrow().getName())
                    .age(user.orElseThrow().getAge())
                    .diaryCount(diaryCount)
                    .email(userEmail)
                    .authType(user.get().getOAuthType())
                    .userProfileImg(user.orElseThrow().getProfileImgUrl())
                    .userRole(user.orElseThrow().getRole().getRoleCode())
                    .communityPostCount(0)
                    .status(user.orElseThrow().getStatus())
                    .build();

        }

        return getUserMyPageDto;


    }

    // 유저가 active인지 아닌지 확인
    public GetUserStatus getUserStatus(String userEmail){
        Optional<User>user=userRepository.findByEmail(userEmail);
        return GetUserStatus.builder()
                .nickname(user.orElseThrow().getName())
                .email(user.orElseThrow().getEmail())
                .status(user.orElseThrow().getStatus())
                .build();

    }

    // 가족 연결 끊기
    public GetFamilyDto deleteFamily(Long familyId){
        Optional<Family>family=familyRepository.findById(familyId);
        family.orElseThrow(()-> new RuntimeException("가족이 존재하지 않습니다")).updateStatus(BaseEntity.Status.DELETE);
        return GetFamilyDto.builder()
                .familyId(familyId)
                .childId(family.orElseThrow().getChild().getId())
                .childName(family.orElseThrow().getChild().getName())
                .parentId(family.orElseThrow().getParent().getId())
                .parentName(family.orElseThrow().getParent().getName())
                .status(family.orElseThrow().getStatus())
                .build();

    }

    // 가족 연결상태 확인
    public GetFamilyDto getFamilyStatus(Long familyId){
        Optional<Family>family=familyRepository.findById(familyId);
        return GetFamilyDto.builder()
                .familyId(familyId)
                .childId(family.orElseThrow().getChild().getId())
                .childName(family.orElseThrow().getChild().getName())
                .parentId(family.orElseThrow().getParent().getId())
                .childImg(family.orElseThrow().getChild().getProfileImgUrl())
                .parentImg(family.orElseThrow().getParent().getProfileImgUrl())
                .parentName(family.orElseThrow().getParent().getName())
                .status(family.orElseThrow().getStatus())
                .build();

    }

    // [로그인] accessToken이랑 이메일만 가지고 로그인

    public PostLoginRes login(PostLoginReq postLoginReq) throws ParseException {
        User.OAuthType oAuthType=postLoginReq.getOAuthType();
        PostLoginRes postLoginRes=null;

        Optional<User> user=userRepository.findByEmail(postLoginReq.getEmail());
        if(oAuthType.equals(User.OAuthType.KAKAO)){
            KakaoUserDto kakaoUserDto=kakaoService.createKakaoUser(postLoginReq.getAccessToken());
            Authentication authentication=new UsernamePasswordAuthenticationToken(kakaoUserDto.getEmail(),null,null);
            TokenDto tokenDto= JwtTokenProvider.generateToken(authentication);
            DecodedJWT decodedJWT= JWT.decode(tokenDto.getRefreshToken());
            redisDao.setValues(authentication,tokenDto.getRefreshToken(),Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())));

           postLoginRes= PostLoginRes.builder()
                    .grantType(tokenDto.getGrantType())
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .code(user.get().getMatchingCode())
                    .profileImg(user.orElseThrow().getProfileImgUrl())
                    .build();



        }else if(oAuthType.equals(User.OAuthType.GOOGLE)){
            GoogleUserDto googleUserDto=googleOAuth.requestUserInfo(postLoginReq.getAccessToken());
            Authentication authentication=new UsernamePasswordAuthenticationToken(googleUserDto.getEmail(),null,null);
            TokenDto tokenDto= JwtTokenProvider.generateToken(authentication);


            // redis에 refresh token 저장
            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            // refresh token을 복호화한 후 만료기한을 redis에 저장할 것
            DecodedJWT decodedJWT= JWT.decode(tokenDto.getRefreshToken());
            System.out.println(decodedJWT.getToken().toString());

            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);

            postLoginRes=PostLoginRes.builder()
                    .grantType(tokenDto.getGrantType())
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .code(user.get().getMatchingCode())
                    .profileImg(user.orElseThrow().getProfileImgUrl())
                    .build();

        }else if(oAuthType.equals(User.OAuthType.APPLE)){
            Authentication authentication=new UsernamePasswordAuthenticationToken(postLoginReq.getEmail(),null,null);
            TokenDto tokenDto= JwtTokenProvider.generateToken(authentication);

            // redis에 refresh token 저장
            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            // refresh token을 복호화한 후 만료기한을 redis에 저장할 것
            DecodedJWT decodedJWT= JWT.decode(tokenDto.getRefreshToken());
            System.out.println(decodedJWT.getToken().toString());

            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);

            postLoginRes= PostLoginRes.builder()
                    .grantType(tokenDto.getGrantType())
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .code(user.get().getMatchingCode())
                    .profileImg(user.orElseThrow().getProfileImgUrl())
                    .build();

        }
        return postLoginRes;
    }

    //리프레쉬토큰 발급

    public PostLoginRes reissue(String userEmail) throws JsonProcessingException {
        String rtkInRedis =redisDao.getValues(userEmail);
        if (Objects.isNull(rtkInRedis)) throw new BaseException(ErrorCode.EXPIRED_AUTHENTICATION);
        System.out.println("redis:"+rtkInRedis);
        String rtkEmail=jwtTokenProvider.getUserEmailFromJWT(rtkInRedis);
        TokenDto tokenDto=jwtTokenProvider.reissueAtk(userEmail,rtkEmail);
        Optional<User> user=userRepository.findByEmail(userEmail);
        return PostLoginRes.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .profileImg(user.orElseThrow().getProfileImgUrl())
                .code(user.orElseThrow().getMatchingCode())
                .build();


    }

    public PostLogoutRes logout(String userEmail,String jwtToken){
        Optional<User> user=userRepository.findByEmail(userEmail);
        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisDao.getValues(userEmail) != null) {
            // Refresh Token 삭제
            redisDao.deleteValues(userEmail);
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(jwtToken);

        redisTemplate.opsForValue()
                .set(jwtToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return  PostLogoutRes.builder()
                .userEmail(user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getEmail())
                .build();

    }




}
