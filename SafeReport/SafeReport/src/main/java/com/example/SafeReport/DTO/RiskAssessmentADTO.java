package com.example.SafeReport.DTO;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiskAssessmentADTO {
	    private int possibilityBefore;
	    private int possibilityAfter;
	    private int importanceBefore;
	    private int importanceAfter;
	    private String workerName;
	    private String supervisor;
	    private String representative;
	    private Boolean essentialActive;
	    private Boolean administrativeActive;
	    private Boolean engineeringActive;
	    private Boolean equipmentActive;
	    private String essentialMeasures;
	    private String administrativeMeasures;
	    private String engineeringMeasures;
	    private String personalEquipment;
	    private String confirmedMeasured;
	    private LocalDate confirmedDate;
	    
}
