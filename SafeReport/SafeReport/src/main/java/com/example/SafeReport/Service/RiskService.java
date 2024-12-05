package com.example.SafeReport.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.SafeReport.DataNotFoundException;
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
	
	/// Report Read
	public Risk getRisk(Integer id) {  
	    Optional<Risk> risk = this.riskRepository.findByReportid(id);
	    
	    if (risk.isPresent()) {
	        return risk.get();
	    } else {
	        throw new DataNotFoundException("question not found");
	    }
	}
	
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
		 List<Sort.Order> sorts = new ArrayList<>();
		 sorts.add(Sort.Order.desc("reportcreatedate"));
		Pageable pageable = PageRequest.of(page, 100, Sort.by(sorts));
	    return riskRepository.findByKeywordAndFilters(keyword, status, riskGrade, pageable);
	}
	
	/// Risk Update
	public void modify(Risk risk, String riskFactor, String riskType, RiskStatus status, RiskGrade riskGrade, String riskDescription, String improvementMeasures) {
		
		risk.setRiskFactor(riskFactor); // 위험요인 (ex. 전기, 화재, 기계 등)
		risk.setRiskType(riskType); // 위험유형 (ex. 화재, 충돌, 낙상 등)
		risk.setStatus(status); // 상태
		risk.setRiskGrade(riskGrade); //위험등금 enum(a, b, c, d)
		risk.setRiskDescription(riskDescription); // 위험내용 (ex. 분전반 화재시 신속 조치 불가 등)
		risk.setImprovementMeasures(improvementMeasures); // 위험개선대책 (ex. 자동소화장치설치)
		
		risk.setLastModifiedDate(LocalDateTime.now()); // 수정시간
		
        this.riskRepository.save(risk);
    }
	
}
