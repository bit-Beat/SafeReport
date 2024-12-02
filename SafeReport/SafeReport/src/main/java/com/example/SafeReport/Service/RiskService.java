package com.example.SafeReport.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.Risk;
import com.example.SafeReport.Enum.RiskGrade;
import com.example.SafeReport.Enum.RiskStatus;
import com.example.SafeReport.Repository.ReportRepository;
import com.example.SafeReport.Repository.RiskRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RiskService {
	private final RiskRepository riskRepository;
	private final ReportRepository reportRepository;
	
	public void creteRisk_FkReport(Report report)
	{
		Risk risk = new Risk(); // risk테이블생성 
		risk.setReportid(report); // fk연결
		risk.setStatus(RiskStatus.PENDING);
		risk.setRiskGrade(RiskGrade.UNDEFINED);
		
        this.riskRepository.save(risk);
	}
	
	// Find Reports with Risks by Keyword(title, reporter_name), Status, RiskGrade
	public Page<Report> getFindRisks(String keyword, RiskStatus status, RiskGrade riskGrade, int page) {
		
		Pageable pageable = PageRequest.of(page, 100);
	    return riskRepository.findByKeywordAndFilters(keyword, status, riskGrade, pageable);
	}
	
}
