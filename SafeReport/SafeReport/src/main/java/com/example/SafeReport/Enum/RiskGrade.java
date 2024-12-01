package com.example.SafeReport.Enum;

public enum RiskGrade {
	UNDEFINED("미지정"),
	D("낮음"),
    C("중간"),
    B("높음"),
    A("매우 높음");

    private final String description;

    RiskGrade(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
