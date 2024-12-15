package com.example.SafeReport.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SafeReport.Entity.Award;
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
	
	/// 수상 저장
	@Transactional
	public void saveAward(Award award) {
	    awardRepository.save(award);
	}

}