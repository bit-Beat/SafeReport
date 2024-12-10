package com.example.SafeReport.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SafeReport.Entity.Award;

public interface AwardRepository extends JpaRepository<Award, Integer>{
	
	 // 특정 년-월과 수상 종류로 조회
    List<Award> findByAwardDateBetweenAndAwardType(LocalDate startDate, LocalDate endDate, String awardType);
}
