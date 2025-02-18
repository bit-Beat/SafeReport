package com.example.SafeReport.Enum;

public enum RiskStatus {
	DENIED("반려"), // 상수값
    PENDING("담당자확인중"), 
    //REVIEWED("확인완료"),
    DISCUSSING("담당부서확인중"),
    IN_PROGRESS("개선조치중"),
    COMPLETED("조치완료");
    
    private final String description;

    RiskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
