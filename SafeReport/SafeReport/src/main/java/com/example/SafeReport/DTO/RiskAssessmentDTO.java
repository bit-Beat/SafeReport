package com.example.SafeReport.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskAssessmentDTO {
	private String category; // 구분
	private String contents; //내용
	private String result; //위험성확인결과
	private String improvement; // 개선내

}


