package com.example.SafeReport.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자를 명시적으로 추가 (JPA 요구사항)
@Entity
public class RiskAssessmentC {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;

	 @ManyToOne
	 @JoinColumn(name = "report_id", nullable = false) // Report 테이블과의 FK 설정
	 private Report reportid;
	 
	 @Column(length = 30)
	 private String category; // 구분
	 
	 @Column(length = 255)
	 private String contents; // 내용
	 
	 @Column(length = 10)
	 private String result; // 위험성확인결과
	 
	 @Column(columnDefinition = "TEXT")
	 private String improvement; // 개선내용
	 
	 private Integer no; // 순서
	 
}

