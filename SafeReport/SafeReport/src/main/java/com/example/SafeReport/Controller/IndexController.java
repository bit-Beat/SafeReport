package com.example.SafeReport.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SafeReport.Entity.Award;
import com.example.SafeReport.Entity.Notice;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Service.AwardService;
import com.example.SafeReport.Service.IndexService;
import com.example.SafeReport.Service.NoticeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {
	private final AwardService awardService;
	private final IndexService indexService;
	private final NoticeService noticeService;
	
	@GetMapping("/home") // 홈 경로
    public String home(Model model) {
		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();
		
		List<Object[]> reportStats = indexService.getReportStatistics();
		Long yearReportCount = indexService.getReportsCount(LocalDateTime.now().getYear(),1,1); /// 올해 제보 건수
		Long monthReportCount = indexService.getReportsCount(LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),1); /// 이번달 제보 건수
		Long dayReportCount = indexService.getReportsCount(LocalDateTime.now().getYear(),LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth()); /// 오늘 제보 건수
		List<Report> report = this.indexService.getList(); // Report
    	List<Notice> activeNotices = noticeService.getActiveNotices(); // active == true인 공지사항
    	List<Notice> notice = this.noticeService.findTop5ByActiveFalseOrderByCreatedateDesc();
    	
		List<Award> bestaward = awardService.getMonthlyAwardsByType(year, month, "최우수상");
		List<Award> betteraward = awardService.getMonthlyAwardsByType(year, month, "우수상"); 
		List<Award> goodaward = awardService.getMonthlyAwardsByType(year, month, "장려상"); 
    	
		// 전체 리스트 결합
	    List<Notice> combinedNotices = new ArrayList<>();
	    combinedNotices.addAll(activeNotices); // 고정 공지사항 먼저 추가
	    combinedNotices.addAll(notice); // 일반 공지사항 추가	    
	    combinedNotices = combinedNotices.size()>5 ? combinedNotices.subList(0, 5) : combinedNotices; // 5개가 넘어가면 5개만 가져오기
    	
    	model.addAttribute("reportList", report);
    	model.addAttribute("notice", combinedNotices);
        model.addAttribute("bestaward", bestaward); // 최우수상
        model.addAttribute("betteraward", betteraward); //우수상
        model.addAttribute("goodaward", goodaward); //장려상
        model.addAttribute("reportStats", reportStats); /// 사별 순위 통계
        model.addAttribute("yearReportCount", yearReportCount); /// 올해 제보 건수
        model.addAttribute("monthReportCount", monthReportCount); /// 이번달 제보 건수
        model.addAttribute("dayReportCount", dayReportCount); /// 오늘 제보 건수
        
    	model.addAttribute("page", "home");
        return "index";  // index.html을 반환
    }
	  
   
    

}
