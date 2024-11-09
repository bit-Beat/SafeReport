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
	private final ReportService reportService;
	
	@GetMapping("/home") // 홈 경로
    public String home(Model model) {
    	List<Report> report = this.indexService.getList();
    	model.addAttribute("reportList", report);
        return "index";  // index.html을 반환
    }
	
    @GetMapping("/") // 루트 경로
    public String root(ReportForm reportForm) {
        return "report";  // report.html을 반환
    }
    
    @GetMapping("/reportlist")
	public String reportlist(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "keyword", defaultValue = "") String keyword) // 모델은 자바와 탬플릿간의 연결고리역할
	{
		Page<Report> paging = this.indexService.getList(page-1, keyword);  // page - 1로 0부터 시작
		model.addAttribute("paging", paging); // 모델 객체에 questionList라는 이름으로 저장했다. 
		model.addAttribute("keyword", keyword);
		return "board/report_list";
	}
    
	@GetMapping(value = "/board/report_detail/{reportid}")
	public String detail(Model model, @PathVariable("reportid") Integer reportid)
	{
		Report report = this.indexService.getReport(reportid);
		model.addAttribute("report", report);
		return "board/report_detail";
	}
    
    @PostMapping("/report/create")
    public String createReport(@Valid ReportForm reportForm, BindingResult bindingResult, Model model)
    {
    	if(bindingResult.hasErrors())
    	{
    		return "report";
    	}
    		
    	this.reportService.create(
                reportForm.getReportTitle(),
                reportForm.getReportDepartment(),
                reportForm.getReporterName(),
                reportForm.getReportLocation(),
                reportForm.getReportContent(),
                reportForm.getReportDetails(),
                reportForm.getReportPassword()
            );
        //model.addAttribute("successMessage", "제보가 성공적으로 접수되었습니다.");
        return "redirect:/?success";
    	//return "redirect:/";
    }
    

}
