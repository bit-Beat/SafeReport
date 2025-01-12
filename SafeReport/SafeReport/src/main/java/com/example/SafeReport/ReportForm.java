package com.example.SafeReport;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportForm {
	
    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max = 255, message = "제목은 최대 255자까지 입력할 수 있습니다.")
    private String reportTitle;

    @NotEmpty(message = "소속은 필수 항목입니다.")
    private String reportDepartment;

    @NotEmpty(message = "이름은 필수 항목입니다.")
    @Size(max = 100, message = "이름은 최대 100자까지 입력할 수 있습니다.")
    private String reporterName;
    
    @NotEmpty(message = "아이디는 필수 항목입니다.")
    @Size(max = 100, message = "아이디는 최대 100자까지 입력할 수 있습니다.")
    private String reporterId;

    @NotEmpty(message = "발생위치는 필수 항목입니다.")
    @Size(max = 255, message = "발생위치는 최대 255자까지 입력할 수 있습니다.")
    private String reportLocation;

    @NotEmpty(message = "신고내용은 필수 항목입니다.")
    private String reportContent;

    @NotEmpty(message = "개선내용은 항목입니다.")
    private String reportDetails; // 개선 요청 내용은 선택 사항이므로 검증은 추가하지 않음

}


