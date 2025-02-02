package com.example.SafeReport.Entity;

import java.time.LocalDateTime;



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
public class Alert {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alertId;
	
	@Column(length = 25)
	private String alertType; // [알림유형] 제보, 패트롤, 공지 등등
	
	@Column(nullable = false, length = 255)
	private String alertTitle; // 알림 제목
	
	@Column(columnDefinition = "TEXT")
	private String url; // 알림 링크 

	@Column(nullable = false)
	private Boolean isRead = false; // 읽음여부

	@Column(nullable = false)
	private Integer priority = 0; // 알림중요도 <높을수록 중요>
	
	@Column(nullable = false, updatable = false) // 알림생성일자는 수정할 필요가 없으므로 updatble = false
	private LocalDateTime alertCreatedate; // 알림 생성일자
	
	@ManyToOne
	@JoinColumn(name = "userid", nullable = false)
	private Users user;

}
