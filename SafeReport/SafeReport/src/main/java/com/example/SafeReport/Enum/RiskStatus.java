package com.example.SafeReport.Enum;

public enum RiskStatus {
    PENDING("담당자확인중"), // 상수값
    REVIEWED("확인완료"),
    DISCUSSING("개선논의중"),
    IN_PROGRESS("개선조치중"),
    COMPLETED("개선조치완료");
    
    private final String description;

    RiskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
