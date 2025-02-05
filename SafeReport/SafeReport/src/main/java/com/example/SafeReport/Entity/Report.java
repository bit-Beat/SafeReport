package com.example.SafeReport.Entity;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 컬럼 속성인 identity 지정
	@Column(name = "report_id") // DB 컬럼 이름과 매핑
	private Integer reportid; 
	
	@Column(nullable = false, length = 255)
	private String report_title;
	
	@Column(nullable = false, length = 100)
	private String reporter_id;
	
	@Column(nullable = false, length = 100)
	private String reporter_name;
	
	@Column(nullable = false, length=100)
	private String report_department; /// 접수부서
	
	@Column(length=100)
	@ColumnDefault("'안전환경팀'") // 기본값 세팅
	private String report_managedepartment; /// 관리부서
	
	@Column(nullable = false, length=255)
	private String report_location;
	
	@Column(nullable = false, length=25)
	private String report_area; // 안전핀포인트 구역
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String report_content;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String report_detail;
	
	@Column(length=50)
	private String report_status;
	
	@Column(name = "report_createdate") // DB 컬럼 이름과 매핑, 실제 db에 들어가는 이름
	private LocalDateTime reportcreatedate;
	
	@Column(name = "report_modifydate") // DB 컬럼 이름과 매핑, 실제 db에 들어가는 이름
	private LocalDateTime reportmodifydate;
	
	@Column(length=255)
	private String attachfile;
	
	@Column(length=255)
	private String attachfile_upload; // 업로드한 파일명
	
	@OneToOne(mappedBy = "reportid", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Risk risk; // 위험성관리

	@OneToOne(mappedBy = "report", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Award award; // 수상테이블
	
	@OneToOne(mappedBy = "reportid", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RiskAssessmentA riskAssessmentA; // 위험성평가 a
	
	@OneToOne(mappedBy = "reportid", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RiskAssessmentB riskAssessmentB; // 위험성평가 b
	
	@OneToMany(mappedBy = "reportid", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<RiskAssessmentC> riskAssessmentC; // 위험성평가 c
	
}
