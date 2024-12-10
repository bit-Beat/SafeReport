package com.example.SafeReport.Controller;

import java.security.Principal;

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
import com.example.SafeReport.Service.RiskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReportController {
	private final IndexService indexService;
	private final ReportService reportService;
	private final RiskService riskService;
	
	@GetMapping("/")
	public String root(ReportForm reportForm, Model model, Principal principal) {
	    // reporterName 값이 비어 있을 때만 로그인한 사용자의 이름을 설정
	    if (principal != null && (reportForm.getReporterName() == null || reportForm.getReporterName().isEmpty())) {
	        reportForm.setReporterName(principal.getName());
	    }
	    model.addAttribute("page", "report"); // 현재 페이지
	    return "board/report";  // report.html 반환
	}
	@PostMapping("/")
	public String createReport(@Valid ReportForm reportForm, BindingResult bindingResult, Model model)
	{
		if(bindingResult.hasErrors())
	  	{
	  		return "board/report";
	  	}
	  		
		Report savedReport = this.reportService.create(
	              reportForm.getReportTitle(),
	              reportForm.getReportDepartment(),
	              reportForm.getReporterName(),
	              reportForm.getReportLocation(),
	              reportForm.getReportContent(),
	              reportForm.getReportDetails(),
	              reportForm.getReportPassword()
	          );

	    // Risk 생성 및 저장
	    this.riskService.creteRisk_FkReport(savedReport);
	  	
	    return "redirect:/?success";
	  	//return "redirect:/";
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
			Report report = this.reportService.getReport(reportid);
			model.addAttribute("report", report);
			return "board/report_detail";
		}

	    @GetMapping("/report/modify/{id}")
	    public String reportModify(ReportForm reportForm, @PathVariable("id") Integer id, Principal principal, Model model) {
	    	Report report = this.reportService.getReport(id);
	        //if(!report.getAuthor().getUsername().equals(principal.getName())) {
	        //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
	        //}
	        reportForm.setReportTitle(report.getReport_title()); // 신고제목
	        reportForm.setReportDepartment(report.getReport_department()); // 소속-
	        reportForm.setReporterName(report.getReporter_name()); //신고자
	        reportForm.setReportLocation(report.getReport_location()); // 발생장소
	        reportForm.setReportContent(report.getReport_content()); //신고내용
	        reportForm.setReportDetails(report.getReport_detail()); //개선요청
	        
	    	model.addAttribute("page", "report"); // 현재 페이지 
	        return "board/report"; /// 신고접수 페이지
	    }
	    
	    @PostMapping("/report/modify/{id}") //// form에 액션을 지정하지 않으면 간은 uri로 요청된다.
	    public String reportModify(@Valid ReportForm reportForm, BindingResult bindingResult,Principal principal, @PathVariable("id") Integer id) {
	        if (bindingResult.hasErrors()) {
	            return "board/report";
	        }
	        Report report = this.reportService.getReport(id);
	        //if (!question.getAuthor().getUsername().equals(principal.getName())) {
	        //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
	        //}
	        this.reportService.modify(report, reportForm.getReportTitle(), reportForm.getReportDepartment(),reportForm.getReporterName(), reportForm.getReportLocation(), reportForm.getReportContent(), reportForm.getReportDetails());
	        return "redirect:/board/report_detail/{id}";
	    }
	    
	    
	    @GetMapping("/report/delete/{id}")
	    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
	        Report report = this.reportService.getReport(id);	     
	        this.reportService.delete(report);	
	        return "redirect:/reportlist";
	    }
	    
	    @PostMapping("/report/delete/{id}")
	    public String questionDelete( @PathVariable("id") Integer id) {
	        Report report = this.reportService.getReport(id);	     
	        this.reportService.delete(report);	
	        return "redirect:/admin/reports";
	    }
	    
	    
}
