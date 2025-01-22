package com.example.SafeReport.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.RiskAssessmentA;


public interface RiskAssessmentARepository extends JpaRepository<RiskAssessmentA, Integer>{
	Optional<RiskAssessmentA> findByReportid(Report report); // optional : 어떤 메서드가 null을 반환할지 확신할 수 없거나, null 처리를 놓쳐서 발생하는 예외를 피할 수 있습니다.
}
