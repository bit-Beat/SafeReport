package com.example.SafeReport.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SafeReport.Entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>{
	List<Notice> findTop5ByOrderByCreatedateDesc(); // 생성날짜순으로 상위 5개 행 가져오기
}
