package com.almostThere.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
public class ErrorResponse {

    private String message;
    private int code;
    
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)  //만약 에러 필드가 null 이라면 response에서 무시
    private List<FieldError> errors; //DTO validation 처리 시 각 필드에 대한 error를 담을 리스트


    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
    }
    // 정적 팩토리 메소드 패턴: 생성자가 아닌 static 메소드로 객체를 반환하는 것
    // 장점: 메소드에 이름을 부여할 수 있음, 캐싱 기능 사용 가능
    // 응답 에러 객체를 생성하는 정적 메소드
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }


}
