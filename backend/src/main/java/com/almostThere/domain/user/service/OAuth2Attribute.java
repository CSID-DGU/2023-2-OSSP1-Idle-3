package com.almostThere.domain.user.service;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String nickname;
    private String profile;
    private String gender;
    private int age;

    static OAuth2Attribute of(String provider, String attributeKey,
        Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                return ofKakao(attributeKey, attributes);
            default:
                throw new RuntimeException();
        }
    }

    /**
     *
     * @param attributeKey OAuth2 유저를 식별하기 위한 Key
     * @param attributes OAuth2 유저 정보
     * @return 필요한 유저 정보
     */
    private static OAuth2Attribute ofKakao(String attributeKey,
        Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");


        return OAuth2Attribute.builder()
            .nickname((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .profile((String) kakaoProfile.get("profile_image_url"))
            .attributes(kakaoAccount)
            .attributeKey(attributeKey)
            .build();
    }

    /**
     *
     * @return User 정보가 들어있는 Map
     */
    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", attributeKey);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("profile", profile);
        map.put("gender", gender);
        map.put("age", age);

        return map;
    }
}