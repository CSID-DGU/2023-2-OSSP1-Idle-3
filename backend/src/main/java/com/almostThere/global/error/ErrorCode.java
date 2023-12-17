package com.almostThere.global.error;

public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "valid check."),
    ALREADY_EXIST_RESOURCE(400, "already exist resource."),
    LOGIN_REQUIRED(401, "login required."),
    EXPIRED_ACCESSTOKEN(401, "expired access-token"),
    NOT_AUTHENTICATION(401, "not authentication"),
    NOT_AUTHORIZATION(403, "not authorization"),
    MEETING_NOT_FOUND(404, "meeting not found."),
    MEETING_MEMBER_NOT_FOUND(404, "meeting-member not found."),
    MEETING_ALREADY_FULLED( 403, "모임의 정원을 초과하였습니다."),
    MEMBER_NOT_FOUND(404, "member not found."),
    TIMEOVER(404, "meeting timeover.");

    private int code;
    private String message;

    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

}
