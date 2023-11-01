package com.almostThere.domain.user.ouath;

import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.domain.user.service.TokenService;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    @Qualifier("redisTemplateForToken")
    private final RedisTemplate redisTemplateForToken;
    @Value("${LOGIN_SUCCESS_URL}")
    private String loginSuccessUrl;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenExpiretime;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("attributes {}", attributes);

        Member member = memberRepository.findByMemberEmail((String) attributes.get("email"));

        if (member == null) { //회원 가입.
            member = Member.builder()
                .memberEmail((String) attributes.get("email"))
                .memberNickname((String) attributes.get("nickname"))
                .memberProfileImg((String) attributes.get("profile"))
                .build();

            memberRepository.save(member);
        }

        //나중에 user탈퇴 기능 생기면 주석 해제
//        if (member.getDelFlag() == 1) { //재 가입 불가능 throw 던지기, Filter ExceptionHandler 작성
//
//            return;
//        }

        Token token = tokenService.generateToken(member, "USER");
        log.info("JwT : {}", token);
        Cookie cookie = new Cookie("refresh-token", token.getRefreshToken());
        // expires in 7 days
        cookie.setMaxAge(60 * 60 * 24 * 14);
        // optional properties
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/token/tokenReissue");

        // add cookie to response
        response.addCookie(cookie);
        redisTemplateForToken.opsForValue() //redis에 refreshToken 저장
            .set(member.getMemberEmail(),
                token.getRefreshToken(),
                refreshTokenExpiretime, //만료 기간
                TimeUnit.MILLISECONDS);

        log.info((String) redisTemplateForToken.opsForValue().get(member.getMemberEmail()));
        response.sendRedirect(loginSuccessUrl + "Bearer " + token.getToken());

//        try {
//
//        } catch (Exception e) {
//
//            log.error("redis error");
//            response.sendRedirect(loginSuccessUrl + "/error");
//        }

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Authorization", "Bearer " + token.getToken());

    }

}