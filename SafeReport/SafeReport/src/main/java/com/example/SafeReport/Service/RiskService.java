package com.example.SafeReport.Service;

import org.springframework.stereotype.Service;

import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.Risk;
import com.example.SafeReport.Enum.RiskGrade;
import com.example.SafeReport.Enum.RiskStatus;
import com.example.SafeReport.Repository.RiskRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RiskService {
	private final RiskRepository riskRepository;
	
	public void creteRisk_FkReport(Report report)
	{
		Risk risk = new Risk(); // risk테이블생성 
		risk.setReportid(report); // fk연결
		risk.setStatus(RiskStatus.PENDING);
		risk.setRiskGrade(RiskGrade.UNDEFINED);
		
        this.riskRepository.save(risk);
	}
}
