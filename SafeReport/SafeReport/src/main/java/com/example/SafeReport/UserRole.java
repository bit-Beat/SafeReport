package com.example.SafeReport;
import lombok.Getter;

@Getter
public enum UserRole {
	ADMIN("ROLE_ADMIN"), // 관리자
	USER("ROLE_USER"); // 유저
	
	UserRole(String value)
	{
		this.value = value;
	}
	
	private String value;
}
