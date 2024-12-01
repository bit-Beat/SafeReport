package com.example.SafeReport.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SafeReport.Entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>{
	List<Notice> findTop5ByActiveFalseOrderByCreatedateDesc(); // 생성날짜순으로 상위 5개 행 가져오기
	Page<Notice> findAll(Pageable pageable); // 페이징 기능
	
    List<Notice> findByActiveTrueOrderByCreatedateDesc(); // active == true인 공지사항 모두 가져오기, Orderby Createdate DESC
    Page<Notice> findByActiveFalse(Pageable pageable); // active == false인 공지사항에만 페이징 적용
}
