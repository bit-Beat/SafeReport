package com.example.SafeReport.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SafeReport.Entity.Award;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.Risk;
import com.example.SafeReport.Enum.RiskGrade;
import com.example.SafeReport.Enum.RiskStatus;
import com.example.SafeReport.Repository.AwardRepository;
import com.example.SafeReport.Service.AwardService;
import com.example.SafeReport.Service.IndexService;
import com.example.SafeReport.Service.ReportService;
import com.example.SafeReport.Service.RiskService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminController {
	private final IndexService indexService;
	private final RiskService riskService;
	private final ReportService reportService;
	private final AwardService awardService;
		
 	/*@GetMapping("/admin/reports")
	public String reportlist(Model model, @RequestParam(value = "keyword", defaultValue = "") String keyword) // 
	{
		List<Report> report = this.indexService.getFindAll();  // page - 1로 0부터 시작
		model.addAttribute("report", report); // 모델 객체에 questionList라는 이름으로 저장했다. 
		return "admin/admin_reports";
	}*/
 	
 	@GetMapping("/admin/reports")  //required = false 시 파라미터가 입력되지 않으면, 변수는 null 값을 가진다.
    public String reportList(Model model, @RequestParam(value="page", defaultValue="1") int page,
    									  @RequestParam(value = "keyword", defaultValue = "") String keyword,
            							  @RequestParam(value = "status", required = false) String status,
            							  @RequestParam(value = "riskGrade", required = false) String riskGrade) {
 		
 	    RiskStatus riskStatus = null;
 	    RiskGrade riskGradeEnum = null;
 	    // Convert status to RiskStatus enum
 	    if (status != null && !status.isEmpty()) {
 	        try {
 	            riskStatus = RiskStatus.valueOf(status);
 	        } catch (IllegalArgumentException e) {
 	            model.addAttribute("errorMessage", "Invalid status value.");
 	        }
 	    }

 	    // Convert riskGrade to RiskGrade enum
 	    if (riskGrade != null && !riskGrade.isEmpty()) {
 	        try {
 	            riskGradeEnum = RiskGrade.valueOf(riskGrade);
 	        } catch (IllegalArgumentException e) {
 	            model.addAttribute("errorMessage", "Invalid risk grade value.");
 	        }
 	    }
 	    
 		Page<Report> report = this.riskService.getFindRisks(keyword, riskStatus, riskGradeEnum, page-1);

 	    // Add attributes to the model
 	    model.addAttribute("report", report);
 	    model.addAttribute("keyword", keyword); // 작성자 or 제목
 	    model.addAttribute("selectedStatus", status); // 상태
 	    model.addAttribute("selectedRiskGrade", riskGrade); // 등급
        return "admin/admin_reports";
    }
 	
 	@GetMapping("/admin/reports/{id}")
 	public String adminReportManage(@PathVariable Integer id, Model model) {
 	   
 	    Report report = this.reportService.getReport(id);
		model.addAttribute("report", report);

 	    return "admin/admin_report_manage";
 	}
 	
 	@PostMapping("/admin/reports/{id}")
 	public String updateRisk(
 	    @PathVariable("id") Integer id,
 	    @RequestParam("riskFactor") String riskFactor,
 	    @RequestParam("riskType") String riskType,
 	    @RequestParam("status") RiskStatus status,
 	    @RequestParam("riskGrade") RiskGrade riskGrade,
 	    @RequestParam("riskDescription") String riskDescription,
 	    @RequestParam("improvementMeasures") String improvementMeasures) {
 	    Risk risk = this.riskService.getRisk(id);
 	    this.riskService.modify(risk, riskFactor, riskType, status, riskGrade, riskDescription, improvementMeasures);
 	    return "redirect:/admin/reports/{id}" ;
 	}
 	
 	////////// Award
	@GetMapping("/admin/award")
    public String adminAward(Model model, @RequestParam(value="page", defaultValue="1") int page,
    		 					@RequestParam(value = "year", required = false) Integer year,
    		 					@RequestParam(value = "month", required = false) Integer month,
    		 					@RequestParam(value = "keyword", defaultValue = "") String keyword) {
		if (year == null) year = LocalDate.now().getYear();
		if (month == null) month = LocalDate.now().getMonthValue();
		Page<Report> paging = riskService.getReportsByYearAndMonth(year, month, keyword, page-1);
		List<Award> bestaward = awardService.getMonthlyAwardsByType(year, month, "최우수상");
		List<Award> betteraward = awardService.getMonthlyAwardsByType(year, month, "우수상"); 
		List<Award> goodaward = awardService.getMonthlyAwardsByType(year, month, "장려상"); 

        // Add data to the model
        model.addAttribute("paging", paging);
        model.addAttribute("bestaward", bestaward); // 최우수상
        model.addAttribute("betteraward", betteraward); //우수상
        model.addAttribute("goodaward", goodaward); //장려상
        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return "admin/admin_award";
	}
	 
	 /*@PostMapping("/admin/award/save")
	 public String saveAwards(@RequestParam("year") int year,
	            			  @RequestParam("month") int month, Model model) {
		 LocalDate awardDate = LocalDate.of(year, month, 1); // 수상 날짜 (년-월 기준)

	     // 기존 데이터를 삭제
	     awardService.deleteAwardsByDate(awardDate);

	     // 최우수상 저장
	     //saveAwardsByType(bestAwardIds, "최우수상", awardDate);

	     // 우수상 저장
	     //saveAwardsByType(betterAwardIds, "우수상", awardDate);

	     // 장려상 저장
	     //saveAwardsByType(goodAwardIds, "장려상", awardDate);
			model.addAttribute("page", "qna");
	        return "qna";  // qna.html을 반환
	     //return "redirect:/admin/award?year=" + year + "&month=" + month ;
	 }*/
	 
	 @PostMapping("/admin/award/save")
	 public String saveAwards(@RequestParam("year") int year,
	         				  @RequestParam("month") int month,
	         				  @RequestParam("awardType") String awardType,
	         				  @RequestParam("awardIds") Integer awardIds, Model model) {
	     
	    LocalDate awardDate = LocalDate.of(year, month, 1); // 수상 날짜 설정
	     
	     // 기존 날짜 데이터 모두 삭제(최우수상, 우수상, 장려상)
	     //awardService.deleteAwardsByDate(awardDate);

	     // 새로운 수상 데이터 저장
	     Integer reportId = awardIds;
	     Report report = reportService.getReport(reportId); // Report 엔티티 조회
	     Award award = new Award();
	     award.setAwardDate(awardDate);
	     award.setAwardType(awardType);
	     award.setReport(report);
	     
	     awardService.saveAward(award); // Award 저장
	     
	     return "redirect:/admin/award?year=" + year + "&month=" + month ;
		/*model.addAttribute("page", "qna");
		return "qna";  // qna.html을 반환*/
	 }

	 @PostMapping("/admin/award/delete")
	 public String deleteAwards(@RequestParam("year") int year,
			  					@RequestParam("month") int month,
			  					@RequestParam("deleteid") Integer deleteid, Model model)
	 {		 
		  
	     // 기존 데이터를 삭제
		 awardService.deleteAwardByReportId(deleteid);
	     return "redirect:/admin/award?year=" + year + "&month=" + month ;
	 }



}
