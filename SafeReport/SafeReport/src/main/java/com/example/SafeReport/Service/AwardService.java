package com.example.SafeReport.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.SafeReport.DataNotFoundException;
import com.example.SafeReport.Entity.Award;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Repository.AwardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class AwardService {
	private final AwardRepository awardRepository;
	
	public List<Award> getMonthlyAwardsByType(int year, int month, String awardType) {
	    LocalDate startDate = LocalDate.of(year, month, 1);
	    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
	    return awardRepository.findByAwardDateBetweenAndAwardType(startDate, endDate, awardType);
	}
	
	@Transactional
    public void deleteAwardsByDate(LocalDate awardDate) {
        awardRepository.deleteByAwardDate(awardDate);
    }
	
	@Transactional
	public void deleteAwardByReportId(Integer reportId) {
	    awardRepository.deleteByReport_Reportid(reportId);
	}
	
	/// 수상 저장
	@Transactional
	public void saveAward(Award award) {
	    awardRepository.save(award);
	}
	
	/// Award Read
	public Award getAward(Integer id) {  
		Optional<Award> award = this.awardRepository.findById(id);
		if (award.isPresent()) {
			return award.get();
		} else {
	        throw new DataNotFoundException("Award not found");
	    }
	}
	
}
