package com.example.SafeReport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public DataNotFoundException(String message) {
        super(message);
    }
}


/*
RuntimeException 클래스를 상속하는 것은 사용자 정의 예외 클래스를 정의하는 방법 중 하나이다. 
RuntimeException은 실행 시 발생하는 예외라는 의미이다.
*/