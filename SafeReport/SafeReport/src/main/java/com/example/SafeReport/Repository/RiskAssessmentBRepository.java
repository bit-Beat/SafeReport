package com.example.SafeReport.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.RiskAssessmentB;


public interface RiskAssessmentBRepository extends JpaRepository<RiskAssessmentB, Integer>{
	Optional<RiskAssessmentB> findByReportid(Report report); // optional : 어떤 메서드가 null을 반환할지 확신할 수 없거나, null 처리를 놓쳐서 발생하는 예외를 피할 수 있습니다. 
}
