package com.example.SafeReport.Controller;

import java.util.List;

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
    	List<Report> report = this.indexService.getList();
    	List<Notice> notice = this.noticeService.getListTop5CreateDateDesc();
    	model.addAttribute("reportList", report);
    	model.addAttribute("notice", notice);
    	model.addAttribute("page", "home");
        return "index";  // index.html을 반환
    }
	  
   
    

}
