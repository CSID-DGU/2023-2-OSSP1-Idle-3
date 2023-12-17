package com.almostThere.global.config;

import com.almostThere.domain.user.ouath.JwtAuthenticationEntryPoint;
import com.almostThere.domain.user.ouath.OAuth2SuccessHandler;
import com.almostThere.domain.user.repository.MemberRepository;
import com.almostThere.domain.user.service.CustomOAuth2UserService;
import com.almostThere.domain.user.service.TokenService;
import com.almostThere.global.filter.JwtAuthFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final JwtAuthenticationEntryPoint jwtEntryPoint;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .httpBasic().disable()      // Http basic Auth  기반으로 로그인 인증창이 뜸.  disable 시에 인증창 뜨지 않음.
            .csrf().disable()       // rest api이므로 csrf 보안이 필요없으므로 disable처리.
            .formLogin().disable() // 로그인 폼 미사용
            .httpBasic().disable() // Http basic Auth 기반으로 열리는 로그인 인증창 미사용
            .exceptionHandling()
//            .authenticationEntryPoint(jwtEntryPoint) //filter에서 잘못된 경우 token 문제임
            .and()
            .sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)// jwt token으로 인증하므로 세션 사용하지 않음. stateless 하도록 처리.
            .and()
            .authorizeRequests()
            .antMatchers("/api/token/**", "/api/oauth2/**", "/api-docs/**", "/swagger-ui/**").permitAll() //토큰 재발급 요청은 제외
//            .anyRequest().authenticated() // 그외의 모든 요청은 인증 필요. => token으로 인증하므로 계속 로그인할 필요 없음.
            .and()
//            .addFilterBefore(new JwtAuthFilter(tokenService, memberRepository),
//                OAuth2LoginAuthenticationFilter.class)
//            .and()// 인증권한이 필요한 페이지.// 나머지 모든 요청 허용  ( 생략 가능 )
//            .formLogin()
            .oauth2Login()
            .authorizationEndpoint().baseUri("/oauth2/authorization")
            .and()
//            .defaultSuccessUrl("/login/oauth2/code")
            .successHandler(successHandler)
            .userInfoEndpoint().userService(customOAuth2UserService);
//            .failureHandler(failureHandler)
//
        http.addFilterAfter(new JwtAuthFilter(tokenService, memberRepository),
            OAuth2LoginAuthenticationFilter.class);

        http.headers().frameOptions().sameOrigin();
        http.cors();
        return http.build();
    }


    /**
     * security filter 를 무시한다.
     * ex) /** => 모든 요청, /static 등
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/**");
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://almostthere.co.kr"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
