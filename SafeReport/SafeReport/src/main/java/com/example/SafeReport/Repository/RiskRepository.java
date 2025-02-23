package com.example.SafeReport.Repository;

import java.util.List;
import java.util.Optional;

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
	
	@Query("SELECT r FROM Risk r WHERE r.reportid.reportid = :reportid")
	Optional<Risk> findByReportid(@Param("reportid") Integer reportid);
	 
	@Query("SELECT r FROM Report r " +
		       "JOIN r.risk k " +
		       "WHERE (:keyword IS NULL OR r.report_title LIKE %:keyword% OR r.reporter_name LIKE %:keyword%) " +
		       "AND (:status IS NULL OR k.status = :status) " +
		       "AND (:riskGrade IS NULL OR k.riskGrade = :riskGrade)"+
			"AND (:manageDepartment IS NULL OR :manageDepartment = '' OR r.report_managedepartment = :manageDepartment)")
	Page<Report> findByKeywordAndFilters(@Param("keyword") String keyword,
		        						 @Param("status") RiskStatus status,
		        						 @Param("riskGrade") RiskGrade riskGrade,
		        						 @Param("manageDepartment") String manageDepartment, // 담당부서
		        						 Pageable pageable);
	
	 @Query("SELECT r FROM Report r WHERE (:keyword IS NULL OR r.report_title LIKE %:keyword% OR r.reporter_name LIKE %:keyword%) AND YEAR(r.reportcreatedate) = :year AND MONTH(r.reportcreatedate) = :month")
	 Page<Report> findByYearAndMonth(Integer year, Integer month, String keyword, Pageable pageable );



}
