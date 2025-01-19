package com.example.SafeReport.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeForm {

    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max = 255, message = "제목은 최대 255자까지 입력할 수 있습니다.")
    private String noticeTitle;

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String noticeContent;
    
    private Boolean active = false; 
    
}
