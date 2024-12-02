package com.example.SafeReport.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.Risk;
import com.example.SafeReport.Enum.RiskGrade;
import com.example.SafeReport.Enum.RiskStatus;

@Repository
public interface RiskRepository extends JpaRepository<Risk, Integer> {
	
	@Query("SELECT r FROM Report r " +
		       "JOIN r.risk k " +
		       "WHERE (:keyword IS NULL OR r.report_title LIKE %:keyword% OR r.reporter_name LIKE %:keyword%) " +
		       "AND (:status IS NULL OR k.status = :status) " +
		       "AND (:riskGrade IS NULL OR k.riskGrade = :riskGrade)")
	Page<Report> findByKeywordAndFilters(@Param("keyword") String keyword,
		        						 @Param("status") RiskStatus status,
		        						 @Param("riskGrade") RiskGrade riskGrade,
		        						 Pageable pageable);

}
