package com.almostThere.global.error.exception;

import com.almostThere.global.error.ErrorCode;

// 내장 예외 객체가 아닌 사용자 정의 예외 객체를 사용하기 위해 작성
// ErrorCode 객체를 통해 같은 에러코드라도 error message를 달리하여 처리 가능
public class AccessDeniedException extends RuntimeException {

    private ErrorCode errorCode;

    public AccessDeniedException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
