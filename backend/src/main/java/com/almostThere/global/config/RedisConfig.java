package com.almostThere.global.config;//package com.ddockddack.domain.member.oauth;

import com.almostThere.domain.chatting.dto.ChattingDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        //redis 추가되면 데이터베이스 추가 ??
////        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
////        config.setDatabase(0); //0번 째 데이터 베이스에서 값 가져오기
//        return new LettuceConnectionFactory(host, port);
//    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setPassword(password); // 비밀번호 설정
        redisConfiguration.setPort(port); // 포트 번호 설정
        redisConfiguration.setHostName(host); // 호스트 설정

        return new LettuceConnectionFactory(redisConfiguration);
    }


    @Bean(name = "redisTemplateForToken")
    public RedisTemplate<String, Object> redisTemplateForToken() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean(name = "redisTemplateForLocation")
    public RedisTemplate<String, ?> redisTemplateForLocation() {
        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean(name = "redisTemplateForChatting")
    public RedisTemplate<?, ?> redisTemplateForChatting() {
        RedisTemplate<String, ChattingDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(ChattingDto.class));
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}