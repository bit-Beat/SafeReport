package com.example.SafeReport.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class) // 모든 예외를 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 상태만 처리
    public String handleException(Exception ex, Model model) {
        String errorMessage = ex.getMessage(); // 예외 메시지 가져오기

        model.addAttribute("errorMessage", errorMessage);
       
        return "error/500";
    }
	
	// 404 에러 처리
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 상태 코드
    public String handle404Exception(Model model) {
        return "error/404";
    }
}