package com.almostThere.domain.user.service;

import com.almostThere.domain.user.entity.Member;
import com.almostThere.domain.user.ouath.Token;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final Environment env;
    @Qualifier("redisTemplateForToken")
    private final RedisTemplate redisTemplateForToken;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private String secretKey;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder()
            .encodeToString(env.getProperty("jwt.token.secret-key").getBytes());
    }

    public Claims setClaims(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getId().toString());

        claims.put("id", member.getId());

        return claims;
    }

    public Token generateToken(Member member, String role) {
        long tokenPeriod = Long.parseLong(env.getProperty("jwt.access-token.expire-length"));
        long refreshPeriod = Long.parseLong(env.getProperty("jwt.refresh-token.expire-length"));

        Claims claims = setClaims(member);
        Claims refreshClaims = setClaims(member);
        claims.put("role", role);
        refreshClaims.put("role", role);

        log.info("getId {}", member.getId().toString());

        Date now = new Date();
        return new Token(
            Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact(),
            Jwts.builder()
                .setClaims(refreshClaims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact());
    }

    public Token generateToken(Member member, String role, String refreshToken) {
        long tokenPeriod = Long.parseLong(
            env.getProperty("jwt.access-token.expire-length")); // 15 min
        Claims claims = setClaims(member);
        claims.put("role", role);

        Date now = new Date();
        return new Token(
            Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact(),
            refreshToken);
    }


    public boolean verifyToken(String token, String email, HttpServletResponse response)
        throws IOException {
        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
            log.info("claims {}", Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token));
            log.info("token verify {}, {},", redisTemplateForToken.keys("*"), redisTemplateForToken.hasKey(email));
            ValueOperations<String, String> logoutValueOperations = redisTemplateForToken.opsForValue();

            if (!redisTemplateForToken.hasKey(email)) {
                log.info("refresh token 없음. -> 재 로그인");
                return false;
            }
            return claims.getBody()
                .getExpiration()
                .after(new Date());
        } catch (ExpiredJwtException e) {
            ((HttpServletResponse) response).sendError(ErrorCode.NOT_AUTHENTICATION.getCode(),
                ErrorCode.NOT_AUTHENTICATION.getMessage());
        } catch (SignatureException | MalformedJwtException e) {
            ((HttpServletResponse) response).sendError(401, "SignatureException error");
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        return true;
    }


    public boolean verifyToken(String token, HttpServletResponse response)
        throws Exception {
        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
//            log.info("claims {}", Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token));
//            log.info("ok {} {}", response.getStatus(), response.getHeaderNames());
//            claims.getBody()
//                .getExpiration()
//                .after(new Date());

            return claims.getBody()
                .getExpiration()
                .after(new Date());
        } catch (ExpiredJwtException e) {
            ((HttpServletResponse) response).sendError(ErrorCode.EXPIRED_ACCESSTOKEN.getCode(),
                ErrorCode.NOT_AUTHENTICATION.getMessage());
            // Exception 핸들링을 추가할 경우 아래 형태로 사용하면 됨.
//            throw new AccessDeniedException(ErrorCode.EXPIRED_ACCESSTOKEN);

            throw new AccessDeniedException(ErrorCode.EXPIRED_ACCESSTOKEN.getMessage());
        } catch (SignatureException | MalformedJwtException e) {
            throw new AccessDeniedException(ErrorCode.NOT_AUTHENTICATION.getMessage());
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new JwtException(ErrorCode.NOT_AUTHENTICATION.getMessage());
        }

    }

    public Long getExpiration(String token) {
        try {
            log.info("expiration {}",
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody());
            Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .getExpiration();
            Long now = new Date().getTime();

            return expiration.getTime() - now;
        } catch (ExpiredJwtException e) {
            return -1L;
        }
    }

    public Long getUid(String token) {
        return Long.valueOf(
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
    }

    public String getRole(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
            .get("role");
    }

    public String reGenerateAccessToken(Cookie[] cookies, HttpServletResponse response)
        throws IOException {
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh-token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            throw new AccessDeniedException(ErrorCode.NOT_AUTHENTICATION.getMessage());
        }

        log.info("reGenerateAccessToken !!!!!! ------------------");
        Long id = getUid(refreshToken);

        //member Respository로 바로 안될 경우 Service에서 새로 만들어서 아래 주석 해제
//        String email = memberService.getMemberById(id).getEmail();

        final Member member = memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        String email = member.getMemberEmail();

        log.info("refresh Token {}, {}", refreshToken, email);

        if (refreshToken.equals("") || !verifyToken(refreshToken, email, response)) {
            throw new AccessDeniedException(ErrorCode.NOT_AUTHENTICATION.getMessage());
        }

        log.info("refreshToken {}, role {}", refreshToken, getRole(refreshToken));

        String role = getRole(refreshToken);

        return generateToken(member, role, refreshToken).getToken();
    }
}