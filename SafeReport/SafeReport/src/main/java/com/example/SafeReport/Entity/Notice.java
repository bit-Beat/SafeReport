package com.example.SafeReport.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 컬럼 속성인 identity 지정
	private Integer noticeid; // 공지사항 ID
	
	@Column(nullable = false, length = 255) //notnull, varchar(255)
	private String title; // 공지사항 제목  	
	
	@Column(nullable = false)
	private String contents; // 공지사항 내용
	
	@CreationTimestamp // Insert시 현재시간 적용 어노테이션
	private LocalDateTime createdate; // 작성일
	
	@UpdateTimestamp // Update시 현재시간 적용 어노테이션
	private LocalDateTime updatedate; // 작성일
	
	@ColumnDefault("0") //default 0
	private Integer viewcount; // 조회수
	
	@ColumnDefault("0") // MSSQL에서 BOOLEAN 대신 TINYINT로 저장 (0: false, 1: true)
	private Boolean active = false; // 고정 여부 true면 고정 false면 고정
}
