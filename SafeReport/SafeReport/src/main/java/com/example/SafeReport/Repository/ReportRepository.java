package com.example.SafeReport.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SafeReport.Entity.Report;



@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
	List<Report> findTop13ByOrderByReportcreatedateDesc();  // 상위 5개 행 가져오기
	Page<Report> findAll(Pageable pageable); // 페이징 기능 
	Page<Report> findAll(Specification<Report> spec, Pageable pageable); // 검색
	
	@Query("SELECT a FROM Report a WHERE a.reporter_name LIKE %:keyword%")
	Page<Report> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("SELECT a FROM Report a WHERE a.report_area LIKE %:area%")
	Page<Report> findAllByArea(@Param("area") String area, Pageable pageable);
	
	// userid로 신고 조회
	@Query("SELECT a FROM Report a WHERE a.reporter_id LIKE %:keyword%")
	Page<Report> findAllByUserid(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("SELECT r.report_department AS department, COUNT(r.report_department) AS count " +
		       "FROM Report r GROUP BY r.report_department ORDER BY COUNT(r.report_department) DESC")
	List<Object[]> findReportStatistics();

	// 올해 신고 건수
	@Query("SELECT COUNT(r) FROM Report r WHERE r.reportcreatedate >= :startOfYear")
	long countReportsThisYear(@Param("startOfYear") LocalDateTime startOfYear);
	

}
