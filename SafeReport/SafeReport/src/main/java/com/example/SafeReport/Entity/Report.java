package com.example.SafeReport.Entity;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private String reporter_name;
	
	@Column(nullable = false, length=100)
	private String report_department;
	
	@Column(nullable = false, length=255)
	private String report_location;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String report_content;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String report_detail;
	
	@Column(nullable = false, length=50)
	private String report_status;
	
	@Column(name = "report_createdate") // DB 컬럼 이름과 매핑, 실제 db에 들어가는 이름
	private LocalDateTime reportcreatedate;
	
	@Column(length=255)
	private String attachfile;
	
	@Column(nullable = false, length=50)
	private String report_pw;
	
}
