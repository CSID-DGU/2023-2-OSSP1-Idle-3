package com.almostThere.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket") // 클라이언트에서 서버로 소켓 연결하는 endPoint 지정
            .setAllowedOriginPatterns("*") // CORS 설정
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/send", "/enter"); // 메시지 수신을 위한 서버 연결 주소 prefix
        registry.setApplicationDestinationPrefixes("/message"); // 메시지 발신을 위한 서버 연결 주소 prefix
    }

    // Socket Controller에 요청이 전달되기 전 처리할 로직을 사용하는 Interceptor 등록 (Config의 StompHandler)
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(stompHandler);
    }
}