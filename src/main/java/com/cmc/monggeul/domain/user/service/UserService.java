package com.cmc.monggeul.domain.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cmc.monggeul.domain.user.dto.KakaoUserDto;
import com.cmc.monggeul.domain.user.dto.PostKakaoLoginReq;
import com.cmc.monggeul.domain.user.entity.Role;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.RoleRepository;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.error.exception.BaseException;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, Object> redisTemplate;

    private final CustomUserDetailService customUserDetailService;

    private final GenerateCertNumber generateCertNumber;




    public TokenDto kakaoLogin(PostKakaoLoginReq postKakaoLoginReq,KakaoUserDto kakaoUserDto) throws BaseException {

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

            // 유저 나이 저장
            User.Age age=Enum.valueOf(User.Age.class, postKakaoLoginReq.getAge());


            User user=User.builder()
                            .name(postKakaoLoginReq.getUserName())
                            .email(kakaoUserDto.getEmail())
                            .role(role)
                            .age(age)
                            .profileImgUrl(kakaoUserDto.getProfileImgUrl())
                            .oAuthType(User.OAuthType.KAKAO)
                            .matchingCode(matchCode)
                            .build();

            userRepository.save(user);

        }

        Authentication authentication=new UsernamePasswordAuthenticationToken(kakaoUserDto.getEmail(),null,null);
        TokenDto tokenDto= JwtTokenProvider.generateToken(authentication);

        // redis에 refresh token 저장
        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        // refresh token을 복호화한 후 만료기한을 redis에 저장할 것
        DecodedJWT decodedJWT= JWT.decode(tokenDto.getRefreshToken());
        System.out.println(decodedJWT.getToken().toString());

//        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);

        return tokenDto;

    }
}
