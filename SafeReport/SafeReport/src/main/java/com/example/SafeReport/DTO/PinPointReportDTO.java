package com.example.SafeReport.DTO;

import java.time.format.DateTimeFormatter;

import com.example.SafeReport.Entity.Report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PinPointReportDTO {
	private Integer reportid;
    private String report_title;
    private String reporter_name;
    private String report_department;
    private String report_createdate;
    private String report_area;
    
    public static PinPointReportDTO fromEntity(Report report) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new PinPointReportDTO(
            report.getReportid(),
            report.getReport_title(),
            report.getReporter_name(),
            report.getReport_department(),
            report.getReportcreatedate().format(formatter),
            report.getReport_area()
        );
    }
}
