package com.almostThere.domain.user.dto;

import lombok.Getter;

@Getter
public class MemberAccessDto {
    private Long id;

    private String accessToken;

    public MemberAccessDto(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}