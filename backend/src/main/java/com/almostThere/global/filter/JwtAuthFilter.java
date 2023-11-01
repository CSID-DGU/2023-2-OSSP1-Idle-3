package com.almostThere.global.filter;

import com.almostThere.domain.user.dto.MemberAccessDto;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.domain.user.service.TokenService;
import com.almostThere.global.error.ErrorCode;
import com.almostThere.global.error.exception.AccessDeniedException;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain)
        throws IOException, ServletException {

        log.info("Filter 진입");
        log.info("요청 타입 {}", request.getMethod());
        log.info("요청 타입 uri {}", request.getRequestURI());

        String accessTokenHeader = request.getHeader("Authorization");
        log.info("accessToken {} ", accessTokenHeader);

        if (request.getRequestURI().startsWith("/api/websocket")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getRequestURI().equals("/api/token/tokenReissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (accessTokenHeader == null || !accessTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            log.warn("JWT token does not begin with Bearer String");
            throw new AccessDeniedException(ErrorCode.NOT_AUTHENTICATION);
        }

        final String accessToken = accessTokenHeader.split(" ")[1].trim();
        try {
            tokenService.verifyToken(accessToken, response);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
//            filterChain.doFilter(request, response);
            return;
        }

//        log.info("@#@#@ ---- ");
        Long id = tokenService.getUid(accessToken);
//        Member member = memberRepository.findById(id)
//            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        log.info("member Res {}", id);

        MemberAccessDto memberAccessDto = new MemberAccessDto(id, accessToken);

//        log.info("member Res {}", member.getId());

        Authentication auth = getAuthentication(memberAccessDto);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }

    public Authentication getAuthentication(MemberAccessDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}