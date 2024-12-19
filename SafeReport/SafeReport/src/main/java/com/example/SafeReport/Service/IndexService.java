package com.example.SafeReport.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.SafeReport.DataNotFoundException;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Repository.ReportRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IndexService {
	private final ReportRepository reportRepository;

	public List<Report> getList(){
		return this.reportRepository.findTop13ByOrderByReportcreatedateDesc();
	}
	
	public List<Report> getFindAll(){
		//return this.reportRepository.findAll();
		return this.reportRepository.findAll(Sort.by(Sort.Direction.DESC, "reportcreatedate"));
	}
	
	
	//페이징 불러오기
	public Page<Report> getList(int page, String keyword)
	{
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("reportcreatedate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.reportRepository.findAllByKeyword(keyword, pageable);
        
        //Specification<Report> spec = reporterNameContains(keyword);
        //return this.reportRepository.findAll(spec, pageable);
	}
	
	/// 검색기능
	public static Specification<Report> reporterNameContains(String keyword) {
	      return (Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
	          if (keyword == null || keyword.isEmpty()) {
	              return criteriaBuilder.conjunction(); // 조건이 없을 경우 모두 반환
	          }
	          return criteriaBuilder.like(root.get("reporterName"), "%" + keyword + "%");
	      };
	  }
	
	/// 통계read
	public List<Object[]> getReportStatistics() {
        return reportRepository.findReportStatistics();
    }
		
}
