package com.example.SafeReport.Enum;
import lombok.Getter;

@Getter
public enum UserRole {
	ADMIN("ROLE_ADMIN"), // 관리자
	ADMINSAFETY("ROLE_ADMINSAFETY"), // 안전관리자
	MANAGER("ROLE_MANAGER"), // 부서관리자
	USER("ROLE_USER"); // 유저
	
	UserRole(String value)
	{
		this.value = value;
	}
	
	private String value;
}
