package com.almostThere.domain.user.controller;

import com.almostThere.domain.user.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/token")
public class TokenController {
    private final TokenService tokenService;
//    private final RedisTemplate redisTemplate;

    @Operation(summary = "Access 토큰 재발급", description = "Access 토큰 재발급 메소드입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "사용자 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/tokenReissue")
    public ResponseEntity refreshAccessToken(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        log.info("refresh 진입");

        Cookie[] cookies = request.getCookies();

        log.info("cookie size {}", cookies);

//        throw new AccessDeniedException(ErrorCode.NOT_AUTHENTICATION);

        String accessToken = "Bearer " + tokenService.reGenerateAccessToken(cookies, response);

        response.addHeader("Authorization", accessToken);
        log.info(accessToken);

        return ResponseEntity.ok().body(accessToken);
    }
}
