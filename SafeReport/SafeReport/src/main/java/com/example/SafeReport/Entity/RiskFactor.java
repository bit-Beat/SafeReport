package com.example.SafeReport.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class RiskFactor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 컬럼 속성인 identity 지정
	private Integer id; 
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false) // Report 테이블과의 FK 설정
	private RiskCategory category; // 위험요인 분류
	
	@Column(length = 100)
	private String factor_name; // 위험요인 (예: 전기, 화재 등)
	
	@Column(columnDefinition = "TEXT")
	private String description; // 위험요인 설명
	
	@CreationTimestamp //Insert시 현재시간 적용 어노테이션
	private LocalDateTime createdate; // 생성일
	
	@UpdateTimestamp //Update시 현재시간 적용 어노테이션
	private LocalDateTime updatedate; // 수정일
	
	
	//category	VARCHAR(50) NOT NULL	위험요인의 분류 (예: 물리적, 화학적, 생물학적, 인간적)
	//hazard_name	VARCHAR(100) NOT NULL	위험요인의 명칭 (예: 전기, 화재, 화학물질 등)
	//description	TEXT	위험요인의 상세 설명
	//createdate	TIMESTAMP DEFAULT CURRENT_TIMESTAMP	데이터 생성 시간
	//updatedate	TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP	데이터 수정 시간
}
