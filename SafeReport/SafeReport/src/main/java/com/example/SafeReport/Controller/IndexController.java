package com.example.SafeReport.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SafeReport.ReportForm;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Service.IndexService;
import com.example.SafeReport.Service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {
	
	private final IndexService indexService;

	
	@GetMapping("/home") // 홈 경로
    public String home(Model model) {
    	List<Report> report = this.indexService.getList();
    	model.addAttribute("reportList", report);
    	model.addAttribute("page", "home");
        return "index";  // index.html을 반환
    }
	  
   
    

}
