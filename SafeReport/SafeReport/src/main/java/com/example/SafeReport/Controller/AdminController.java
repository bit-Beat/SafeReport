package com.example.SafeReport.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Service.IndexService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminController {
	private final IndexService indexService;
	
 	@GetMapping("/admin/reports")
	public String reportlist(Model model) // 
	{
		List<Report> report = this.indexService.getFindAll();  // page - 1로 0부터 시작
		model.addAttribute("report", report); // 모델 객체에 questionList라는 이름으로 저장했다. 
		return "admin/admin_reports";
	}
}
