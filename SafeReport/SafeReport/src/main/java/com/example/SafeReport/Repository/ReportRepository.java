package com.example.SafeReport.Repository;

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

}
