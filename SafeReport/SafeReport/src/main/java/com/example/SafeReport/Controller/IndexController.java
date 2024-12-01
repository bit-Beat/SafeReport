package com.example.SafeReport.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SafeReport.Entity.Notice;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Service.IndexService;
import com.example.SafeReport.Service.NoticeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {
	
	private final IndexService indexService;
	private final NoticeService noticeService;
	
	@GetMapping("/home") // 홈 경로
    public String home(Model model) {
    	List<Report> report = this.indexService.getList(); // Report
    	
    	List<Notice> activeNotices = noticeService.getActiveNotices(); // active == true인 공지사항
    	List<Notice> notice = this.noticeService.findTop5ByActiveFalseOrderByCreatedateDesc();

		// 전체 리스트 결합
	    List<Notice> combinedNotices = new ArrayList<>();
	    combinedNotices.addAll(activeNotices); // 고정 공지사항 먼저 추가
	    combinedNotices.addAll(notice); // 일반 공지사항 추가	    
	    combinedNotices = combinedNotices.size()>5 ? combinedNotices.subList(0, 5) : combinedNotices; // 5개가 넘어가면 5개만 가져오기
    	
    	model.addAttribute("reportList", report);
    	model.addAttribute("notice", combinedNotices);
    	model.addAttribute("page", "home");
        return "index";  // index.html을 반환
    }
	  
   
    

}
