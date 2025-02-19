package com.example.SafeReport.Controller;



import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SafeReport.DTO.ReportForm;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.Users;
import com.example.SafeReport.Service.IndexService;
import com.example.SafeReport.Service.ReportService;
import com.example.SafeReport.Service.RiskService;
import com.example.SafeReport.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
public class ReportController {
	private final IndexService indexService;
	private final ReportService reportService;
	private final RiskService riskService;
	private final UserService userService;
	
	@GetMapping("/")
	public String root(ReportForm reportForm, Model model, Principal principal) {
	    // reporterName 값이 비어 있을 때만 로그인한 사용자의 이름을 설정
	    if (principal != null && (reportForm.getReporterName() == null || reportForm.getReporterName().isEmpty())) {
	    	Users users = userService.getUser(principal.getName());
	    	reportForm.setReporterName(users.getUsername());
	    	reportForm.setReportDepartment(users.getDepartment());
	    	reportForm.setReporterId(users.getUserid());
	    }
	    
	    model.addAttribute("page", "report"); // 현재 페이지
	    return "board/report";  // report.html 반환
	}
	
	@PostMapping("/")
	public String createReport(@Valid ReportForm reportForm, BindingResult bindingResult, Model model , HttpServletRequest request)
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
	        		reportForm.getReporterId(),
	        		reportForm.getPhoto());
	        
	    // Risk 생성 및 저장
	    this.riskService.creteRisk_FkReport(savedReport);
	    
	    //return "redirect:/?success";
	    return "message/report_complete";
	}
	
	/// 내 제보 보기
 	@GetMapping("/myreports")
	public String reportlist(Model model, @RequestParam(value="page", defaultValue="1") int page, Principal principal) // 모델은 자바와 탬플릿간의 연결고리역할
	{
 		String keyword = userService.getUser(principal.getName()).getUsername(); // 유저명가져오기
		Page<Report> paging = this.indexService.getList_userid(page-1, principal.getName());  // page - 1로 0부터 시작
		model.addAttribute("paging", paging); // 모델 객체에 questionList라는 이름으로 저장했다. 
		model.addAttribute("keyword", keyword);
		return "board/myreport_list";
	}
	
	@GetMapping("/reportlist")
	public String reportlist(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "keyword", defaultValue = "") String keyword) // 모델은 자바와 탬플릿간의 연결고리역할
	{
		Page<Report> paging = this.indexService.getList(page-1, keyword);  // page - 1로 0부터 시작
		model.addAttribute("paging", paging); // 모델 객체에 questionList라는 이름으로 저장했다. 
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", "reportlist");
		return "board/report_list";
	}
	
	@GetMapping(value = "/board/report_detail/{reportid}")
	public String detail(Model model, @PathVariable("reportid") Integer reportid, Principal principal)
	{
		Report report = this.reportService.getReport(reportid);
		Users user = userService.getUser(principal.getName());
		
		model.addAttribute("report", report);
		model.addAttribute("user", user);
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
	    reportForm.setReporterId(report.getReporter_id()); // 아이디
	    reportForm.setExistingPhotoName(report.getAttachfile()); // 기존 이미지 파일
	    reportForm.setUuidPhotoName(report.getAttachfile_upload()); // 고유값 이미지 파일명
	    
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
	    this.reportService.modify(report, reportForm.getReportTitle(), reportForm.getReportDepartment(),reportForm.getReporterName(), reportForm.getReportLocation(), reportForm.getReportContent(), reportForm.getReportDetails(), reportForm.getPhoto());
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
	
	@PostMapping("/report/modify/filedelete/{id}") /// 파일 삭제 ajax
	@ResponseBody
	public ResponseEntity<Map<String, Object>> report_fileDelete(@PathVariable("id") Integer id, @RequestBody Map<String, Object> request)
	{
	    // ID로 Report 조회
	    Report report = this.reportService.getReport(id);
	    if (report == null) return ResponseEntity.badRequest().body(Map.of("error", "Report not found."));
	    
	    // 파일 삭제 서비스 호출
	    Map<String, Object> response = new HashMap<>();
	    if(this.reportService.file_delete(report))
	    	response.put("success", true);	    	
	    else
	    	response.put("success", false);
	    
		return ResponseEntity.ok(response); // json 응답 반환
	}
	    
	    /*
	    @PostMapping("/report/passwordcompare")
	    public String passwordCompare(@RequestParam("id") int id, @RequestParam("password") int password, Model model) {
	        Report report = this.reportService.getReport(id);
	        if (this.reportService.reportComparePassword(report, "1")) {
	            return "redirect:/report/modify/" + id;
	        } else {
	            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
	            model.addAttribute("report", report);
	            return "board/report_detail";
	        }
	    }
	    */
	    /*@PostMapping("/report/passwordcompare")
	    @ResponseBody
	    public ResponseEntity<Map<String, Object>> passwordCompare(@RequestBody Map<String, Object> request) {
	        // 요청에서 id와 password 가져오기
	        String id = (String) request.get("id");
	        String password = (String) request.get("password");

	        // 보고서 가져오기
	        Report report = this.reportService.getReport(Integer.parseInt(id));

	        // 응답 데이터 구성
	        Map<String, Object> response = new HashMap<>();
	        if (this.reportService.reportComparePassword(report, password)) {
	            response.put("success", true);
	            response.put("redirectUrl", "/report/modify/" + id); // 수정 페이지 URL
	        } else {
	            response.put("success", false);
	            response.put("error", "비밀번호가 일치하지 않습니다.");
	            response.put("password", password);
	            response.put("id", id);
	        }

	        // JSON 응답 반환
	        return ResponseEntity.ok(response);
	    }*/
 
}
