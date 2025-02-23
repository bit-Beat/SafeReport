package com.example.SafeReport.Entity;

import java.time.LocalDateTime;

import com.example.SafeReport.Enum.RiskGrade;
import com.example.SafeReport.Enum.RiskStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer riskId;

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false) // Report 테이블과의 FK 설정
    private Report reportid;

    @Column(length = 100)
    private String riskFactor; // 위험요인 (ex. 전기, 화재, 기계 등)

    @Column(length = 100)
    private String riskType; // 위험유형 (ex. 화재, 충돌, 낙상 등)
    
    @Enumerated(EnumType.STRING) // ENUM을 문자열로 저장
    @Column(length = 10)
    private RiskGrade riskGrade; // 위험등급 (ex. A, B, C, D)

    /*@Column(columnDefinition = "TEXT")
    private String riskDescription; // 위험내용 (ex. 분전반 화재시 신속 조치 불가 등)

    @Column(columnDefinition = "TEXT")
    private String improvementMeasures; // 위험개선대책 (ex. 자동소화장치설치)*/

    @Enumerated(EnumType.STRING) // ENUM을 문자열로 저장
    @Column(length = 50)
    private RiskStatus status; // 상태
    
    @Column(length = 50)
    private String rejectMessage; //반려사유
    
    @Column(length = 100)
	private String acceptId; // 접수자 ID

    private LocalDateTime lastModifiedDate; // 수정일
}
