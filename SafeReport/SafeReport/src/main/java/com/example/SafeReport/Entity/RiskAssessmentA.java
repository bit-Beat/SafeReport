package com.example.SafeReport.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RiskAssessmentA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 컬럼 속성인 identity 지정
	private Integer riskAssessmentBId; 
	
    @OneToOne
    @JoinColumn(name = "report_id", nullable = false) // Report 테이블과의 FK 설정
    private Report reportid;
    
    private Integer possibilityBefore; // 가능성 (개선 전)
    private Integer possibilityAfter; // 가능성 (개선 후)
    
    private Integer importanceBefore; // 중대성 (개선 전)
    private Integer importanceAfter; // 중대성 (개선 후)
    
    @Column(length = 10)
    private String supervisorName; // 관리감독자
    
    @Column(length = 10)
    private String representativeName; // 수급인 대표
    
    @Column(length = 10)
    private String workerName; // 작업자
    
    @Column(columnDefinition = "TEXT")
    private String essential_measures; //본질적 대책 내용
    
    @ColumnDefault("0") // MSSQL에서 BOOLEAN 대신 TINYINT로 저장 (0: false, 1: true)
	private Boolean essential_checked = false; // 본질적 대책 여부
    
    @Column(columnDefinition = "TEXT")
    private String administrative_measures; //공학적 대책 내용
    
    @ColumnDefault("0") // MSSQL에서 BOOLEAN 대신 TINYINT로 저장 (0: false, 1: true)
	private Boolean administrative_checked = false; // 공학적 대책 여부
    
    @Column(columnDefinition = "TEXT")
    private String engineering_measures; //관리적 대책 내용
    
    @ColumnDefault("0") // MSSQL에서 BOOLEAN 대신 TINYINT로 저장 (0: false, 1: true)
	private Boolean engineering_checked = false; // 관리적 대책 여부
    
    @Column(columnDefinition = "TEXT")
    private String personal_equipment; // 개인보호구 내용
    
    @ColumnDefault("0") // MSSQL에서 BOOLEAN 대신 TINYINT로 저장 (0: false, 1: true)
	private Boolean equipment_checked = false; // 개인보호구 여부

    @Column(length = 255)
    private String confirmed_measured; // 확정대책
    
    private LocalDate confirmed_date; // 조치 예정일

    private LocalDateTime createdate; //생성일
    private LocalDateTime modifiedDate; // 수정일
}
