package com.example.SafeReport.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.RiskAssessmentC;

import jakarta.transaction.Transactional;

public interface RiskAssessmentCRepository extends JpaRepository<RiskAssessmentC, Integer> {
	List<RiskAssessmentC> findByReportid(Report report); // optional : 어떤 메서드가 null을 반환할지 확신할 수 없거나, null 처리를 놓쳐서 발생하는 예외를 피할 수 있습니다.

    // report_id에 해당하는 Award 삭제
	@Modifying
	@Transactional // 예외 발생 시 Rollback 성공시 commit
    @Query("DELETE FROM RiskAssessmentC r WHERE r.reportid = :report")
    void deleteByReport(@Param("report") Report report);
}
