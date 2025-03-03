package com.example.SafeReport.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RiskAssessmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    @Column(nullable = false, length = 255)
    private String fileName; // 파일명
    
	@Column(length=255)
	private String fileUploadName; // 업로드한 파일명

    @Column(nullable = false, length = 500)
    private String filePath; // 파일 경로 (서버 저장 경로)

    @Column(nullable = false, length = 50)
    private String fileType; // 파일 타입 (ex: PDF, PNG, JPG)

    @Column(nullable = false)
    private Long fileSize; // 파일 크기 (byte 단위)

    @Column(length = 100)
    private String uploadedBy; // 업로드한 사용자 ID

    private LocalDateTime uploadDate; // 업로드 일자
    
    @Column(nullable = false, length = 1)
    private String assessmentType; // 'A', 'B', 'C' 중 하나를 저장 (평가 유형 구분)

    // 외래키 설정 (RiskAssessmentA, B, C 중 하나와 연관)
    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false) // Report 테이블과의 FK 설정
    private Report reportid;
}

