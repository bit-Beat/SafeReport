package com.example.SafeReport.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.SafeReport.DTO.RiskAssessmentADTO;
import com.example.SafeReport.DTO.RiskAssessmentDTO;
import com.example.SafeReport.Entity.Award;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.Risk;
import com.example.SafeReport.Entity.RiskAssessmentB;
import com.example.SafeReport.Entity.RiskAssessmentC;
import com.example.SafeReport.Entity.RiskCategory;
import com.example.SafeReport.Entity.RiskFactor;
import com.example.SafeReport.Entity.Users;
import com.example.SafeReport.Enum.RiskGrade;
import com.example.SafeReport.Enum.RiskStatus;
import com.example.SafeReport.Repository.RiskCategoryRepository;
import com.example.SafeReport.Repository.RiskFactorRepository;
import com.example.SafeReport.Service.AwardService;
import com.example.SafeReport.Service.ReportService;
import com.example.SafeReport.Service.RiskService;
import com.example.SafeReport.Service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminController {
	private final RiskService riskService;
	private final ReportService reportService;
	private final AwardService awardService;
	private final UserService userService;
	
	private final RiskFactorRepository riskFactorRepository; // 위험요인 table
	private final RiskCategoryRepository riskCategoryRepository; // 위험카테고리 Table
	
 	@GetMapping("/admin/reports")  //required = false 시 파라미터가 입력되지 않으면, 변수는 null 값을 가진다.
    public String reportList(Model model, @RequestParam(value="page", defaultValue="1") int page,
    									  @RequestParam(value = "keyword", defaultValue = "") String keyword,
            							  @RequestParam(value = "status", required = false) String status,
            							  @RequestParam(value = "riskGrade", required = false) String riskGrade,
            							  @RequestParam(value = "manageDepartment", required=false) String manageDepartment,
            							  Principal principal) {
 		
 		Users user = userService.getUser(principal.getName());
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
 	    
 	    if(!user.getRole().equals("adminsafety") && !user.getRole().equals("admin")) // 보안관리자 안전담당자가 아니면 자기 부서로만 검색 가능
 	    	manageDepartment = user.getDepartment();
 	    
 		Page<Report> report = this.riskService.getFindRisks(keyword, riskStatus, riskGradeEnum, manageDepartment,page-1);

 	    // Add attributes to the model
 	    model.addAttribute("report", report);
 	    model.addAttribute("keyword", keyword); // 작성자 or 제목
 	    model.addAttribute("selectedStatus", status); // 상태
 	    model.addAttribute("selectedRiskGrade", riskGrade); // 등급
 	    model.addAttribute("selectedDepartment", manageDepartment); // 등급
 	    model.addAttribute("user", user); // 유저정보
        return "admin/admin_reports";
    }
 	
 	@GetMapping("/admin/reports/{id}")
 	public String adminReportManage(@PathVariable Integer id, Model model) {
 	   
 	    Report report = this.reportService.getReport(id);
 	    List<RiskCategory> riskCategory = riskCategoryRepository.findAll(); // 모든 위험카테고리 조회
        List<RiskFactor> riskFactor = riskFactorRepository.findAll(); //모든 위험요인 조회
        
        // 카테고리 중복 제거된 카테고리 목록 생성
        /*List<String> uniqueCategories = riskFactor.stream()
                .map(RiskFactor::getCategory) // category 필드만 추출
                .distinct() // 중복 제거
                .collect(Collectors.toList());*/
        
        //model.addAttribute("uniqueCategories", uniqueCategories); // 위험요인
        model.addAttribute("riskCategory", riskCategory); // 위험카테고리
        model.addAttribute("riskFactor", riskFactor); // 위험유형
        model.addAttribute("report", report);
 	    return "admin/admin_report_detail";
 	}
 	
 	@GetMapping("/admin/reportsManage/{id}") /// 위험성평가
 	public String adminReportManage2(@PathVariable Integer id, Model model) {
 		Report report = this.reportService.getReport(id);
 		Optional<RiskAssessmentB>  riskassessmentB = this.riskService.getRiskB_Reportid(report);
		
		riskassessmentB.ifPresent(riskB -> model.addAttribute("riskB", riskB)); // optional의 메소드인 ifPresent를 이용해 값이 있을경우에만 Model추가
		// RiskAssessmentC 데이터가 비어 있는 경우 기본 데이터를 제공
		// RiskAssessmentC 데이터가 비어 있는 경우 기본 데이터를 제공
        if (report.getRiskAssessmentC() == null || report.getRiskAssessmentC().isEmpty()) {
            List<RiskAssessmentC> defaultAssessments = new ArrayList<>();

            // 기본 생성자를 통해 객체 생성 후 필드 값 설정
            RiskAssessmentC assessment1 = new RiskAssessmentC();
            assessment1.setCategory("인터뷰");
            assessment1.setContents("작업자의 위험요인에 대한 인식은 잘 되어있는가?");
            assessment1.setResult("");
            assessment1.setImprovement("");

            RiskAssessmentC assessment2 = new RiskAssessmentC();
            assessment2.setCategory("안전장치");
            assessment2.setContents("위험요인에 대한 안전 장치는 설치되어 있는가?");
            assessment2.setResult("");
            assessment2.setImprovement("");

            RiskAssessmentC assessment3 = new RiskAssessmentC();
            assessment3.setCategory("안전수칙");
            assessment3.setContents("위험요인에 대한 안전 수칙 기준이 있는가?");
            assessment3.setResult("");
            assessment3.setImprovement("");

            RiskAssessmentC assessment4 = new RiskAssessmentC();
            assessment4.setCategory("안전보호구");
            assessment4.setContents("위험요인에 대한 보호구가 지급되고 있는가?");
            assessment4.setResult("");
            assessment4.setImprovement("");

            RiskAssessmentC assessment5 = new RiskAssessmentC();
            assessment5.setCategory("안전표지");
            assessment5.setContents("위험요인에 대한 안전 표지가 부착되어 있는가?");
            assessment5.setResult("");
            assessment5.setImprovement("");

            // 리스트에 추가
            defaultAssessments.add(assessment1);
            defaultAssessments.add(assessment2);
            defaultAssessments.add(assessment3);
            defaultAssessments.add(assessment4);
            defaultAssessments.add(assessment5);

            report.setRiskAssessmentC(defaultAssessments);
        }
        
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
 	    @RequestParam("reportDepartment") String reportDepartment
 	    ) {
 	    Risk risk = this.riskService.getRisk(id);
 	    Report report = this.reportService.getReport(id);
 	    
 	    this.riskService.modify(risk, report, riskFactor, riskType, status, riskGrade, reportDepartment, null);
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
	 
	 /*@PostMapping("/admin/reportsManage/Agrade/{id}")
	 public String riskEvaluation_A(@PathVariable("id") Integer reportid,@RequestParam(value="possibilityBefore_A", defaultValue = "0") String possibilityBefore_A
									,@RequestParam(value="possibilityAfter_A", defaultValue = "0") String possibilityAfter_A
									,@RequestParam(value="importanceBefore_A", defaultValue = "0") String importanceBefore_A
									,@RequestParam(value="importanceAfter_A", defaultValue = "0") String importanceAfter_A
									,@RequestParam("supervisorA") String supervisorA
									,@RequestParam("representativeA") String representativeA
									,@RequestParam(value = "essentialActiveA", required = false, defaultValue = "false") Boolean essentialActiveA 
									,@RequestParam(value = "administrativeActiveA", required = false, defaultValue = "false") Boolean administrativeActiveA
									,@RequestParam(value = "engineeringActiveA", required = false, defaultValue = "false") Boolean engineeringActiveA
									,@RequestParam(value = "equipmentActiveA", required = false, defaultValue = "false") Boolean equipmentActiveA
									,@RequestParam("essential_measuresA") String essential_measuresA
									,@RequestParam("administrative_measuresA") String administrative_measuresA
									,@RequestParam("engineering_measuresA") String engineering_measuresA
									,@RequestParam("personal_equipmentA") String personal_equipmentA
									,@RequestParam("confirmed_measuredA") String confirmed_measuredA
									,@RequestParam(value = "confirmed_dateA", required = false) String confirmedDateString)
	 {
		 Report report = this.reportService.getReport(reportid);
		 Map<String, Object> response = new HashMap<>(); // 응답데이터 구성
		 
		 // 공백이면 0
		 int possibilityBefore = Integer.parseInt(possibilityBefore_A);
		 int possibilityAfter = Integer.parseInt(possibilityAfter_A);
		 
		 int importanceBefore = Integer.parseInt(importanceBefore_A);
		 int importanceAfter = Integer.parseInt(importanceAfter_A);
		 
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 수정 ㅎ해야함 id가 아닌 유저명으로 하도록
		 String loggedInUsername = authentication.getName(); // // 수정 ㅎ해야함 id가 아닌 유저명으로 하도록
		 
		 LocalDate confirmedDate = null;// = LocalDate.parse(confirmedDateString);
		 if(!confirmedDateString.equals(""))
			 confirmedDate = LocalDate.parse(confirmedDateString);
		 
		 // Service를 호출하여 저장 또는 업데이트
	     riskService.saveOrUpdateRiskAssessmentA(report, possibilityBefore, possibilityAfter, importanceBefore, importanceAfter, loggedInUsername, supervisorA, representativeA
	    		 								,essentialActiveA, administrativeActiveA, engineeringActiveA, equipmentActiveA
	    		 								,essential_measuresA, administrative_measuresA, engineering_measuresA, personal_equipmentA
	    		 								,confirmed_measuredA, confirmedDate);
		 
		 return "redirect:/admin/reportsManage/" + reportid;
	 }*/
	 
	 @PostMapping("/admin/reportsManage/Bgrade/{id}")
	 public String riskEvaluation_B(@PathVariable("id") Integer reportid
			 						,@RequestParam("possibilityBefore_B") String possibilityBefore_B
	 								,@RequestParam("possibilityAfter_B") String possibilityAfter_B
	 								,@RequestParam("supervisorB") String supervisorB
	 								,@RequestParam("representativeB") String representativeB
	 								,@RequestParam(value = "essentialActiveB", required = false, defaultValue = "false") Boolean essentialActiveB 
	 								,@RequestParam(value = "administrativeActiveB", required = false, defaultValue = "false") Boolean administrativeActiveB
	 								,@RequestParam(value = "engineeringActiveB", required = false, defaultValue = "false") Boolean engineeringActiveB
	 								,@RequestParam(value = "equipmentActiveB", required = false, defaultValue = "false") Boolean equipmentActiveB
	 								,@RequestParam("essential_measuresB") String essential_measuresB
	 								,@RequestParam("administrative_measuresB") String administrative_measuresB
	 								,@RequestParam("engineering_measuresB") String engineering_measuresB
	 								,@RequestParam("personal_equipmentB") String personal_equipment
	 								,@RequestParam("confirmed_measured") String confirmed_measured
	 								,@RequestParam(value = "confirmed_date", required = false) String confirmedDateString
	 								)
	 {
		 Report report = this.reportService.getReport(reportid);
		 
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 수정 ㅎ해야함 id가 아닌 유저명으로 하도록
		 String loggedInUsername = authentication.getName(); // // 수정 ㅎ해야함 id가 아닌 유저명으로 하도록
		 
		 LocalDate confirmedDate = null;// = LocalDate.parse(confirmedDateString);
		 if(!confirmedDateString.equals(""))
			 confirmedDate = LocalDate.parse(confirmedDateString);
		 
		 // Service를 호출하여 저장 또는 업데이트
	     riskService.saveOrUpdateRiskAssessmentB(report, possibilityBefore_B, possibilityAfter_B, loggedInUsername, supervisorB, representativeB
	    		 								,essentialActiveB, administrativeActiveB, engineeringActiveB, equipmentActiveB
	    		 								,essential_measuresB, administrative_measuresB, engineering_measuresB, personal_equipment
	    		 								,confirmed_measured, confirmedDate);
		 								
		 return "redirect:/admin/reportsManage/" + reportid;
	 }
	 
	 @PostMapping("/admin/reportsManage/Cgrade/{reportId}")
	 @ResponseBody
	 public ResponseEntity<Map<String, Object>> riskEvaluation_C(
			 @PathVariable("reportId") int reportId,
			 @RequestPart("data") List<RiskAssessmentDTO> requests, // JSON 데이터
			 @RequestPart(value = "files", required = false) List<MultipartFile> files ){
		 
		 Report report = this.reportService.getReport(reportId);
		 
		 Map<String, Object> response = new HashMap<>(); // 응답데이터 구성
		 
		 try
		 {
			 riskService.RiskC_DeleteReport(report); // 기존데이터삭제
			 
			 int no = 1;
			 for(RiskAssessmentDTO request : requests)
			 {
				 riskService.RiskC_save(request, report, no);
				 no++;
			 }
			 
			 // 파일 저장 처리
			 if (files != null && !files.isEmpty()) {
				for (MultipartFile file : files) {
					riskService.saveRiskFile(file, report, "C"); // 'C' 등급 파일로 저장
				}
			 }
			 response.put("success", true);
		 }
		 catch (Exception e)
		 {
			 response.put("success", false);
			 response.put("error", e);
		 }
		 
		 return ResponseEntity.ok(response);
	 }
	 
	 @PostMapping("/admin/reportsManage/Agrade/{reportid}")
	 @ResponseBody
	 public ResponseEntity<Map<String, Object>> riskEvaluation_A(
	     @PathVariable("reportid") int reportid, 
	     @RequestPart("data") RiskAssessmentADTO request,
	     @RequestPart(value = "photo", required = false) MultipartFile[] files) {

	     Report report = this.reportService.getReport(reportid);
	     Map<String, Object> response = new HashMap<>();
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     String loggedInUsername = authentication.getName();

	     try {
	    	 riskService.saveOrUpdateRiskAssessmentA(
		         report,
		         request.getPossibilityBefore(),
		         request.getPossibilityAfter(),
		         request.getImportanceBefore(),
		         request.getImportanceAfter(),
		         loggedInUsername,
		         request.getSupervisor(),
		         request.getRepresentative(),
		         request.getEssentialActive(),
		         request.getAdministrativeActive(),
		         request.getEngineeringActive(),
		         request.getEquipmentActive(),
		         request.getEssentialMeasures(),
		         request.getAdministrativeMeasures(),
		         request.getEngineeringMeasures(),
		         request.getPersonalEquipment(),
		         request.getConfirmedMeasured(),
		         request.getConfirmedDate(),
		         files // 파일 추가
	    			 );
	    	 response.put("success", true);
	     } catch (Exception e) {
	         response.put("success", false);
	         response.put("error", e.getMessage());
	     }

	     return ResponseEntity.ok(response);
	 }

	 
	@PostMapping("/admin/reports/ajax/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateRisk_Ajax(@PathVariable("id") Integer id, @RequestBody Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();

		try {
		    String riskFactor = (String) request.get("riskFactor");
		    String riskType = (String) request.get("riskType");
		    String reportDepartment = (String) request.get("reportDepartment");

		    //Enum 값 변환
		    //RiskStatus status = RiskStatus.DISCUSSING;
		    RiskGrade riskGrade = RiskGrade.valueOf((String) request.get("riskGrade"));

		    // 엔티티 조회
		    Risk risk = this.riskService.getRisk(id);
		    Report report = this.reportService.getReport(id);

		    //데이터 수정
		    this.riskService.modify(risk, report, riskFactor, riskType, RiskStatus.DISCUSSING, riskGrade, reportDepartment, null);

		    //성공 응답
		    response.put("success", true);
		    response.put("message", "데이터가 성공적으로 저장되었습니다.");
		    return ResponseEntity.ok(response);

		} 
		catch (Exception e) {
			response.put("success", false);
		    response.put("error", e.getMessage());
		    return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping("/admin/reports/reject")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Reject_report(@RequestBody Map<String, String> request){
		Map<String, Object> response = new HashMap<>();
		
		try 
		{
			int id = Integer.parseInt(request.get("reportId")); 
			Risk risk = this.riskService.getRisk(id);
			Report report = this.reportService.getReport(id);
			RiskGrade riskGrade = RiskGrade.valueOf("UNDEFINED");
			
			//데이터 수정
		    this.riskService.modify(risk, report, "", "", RiskStatus.DENIED, riskGrade, "안전환경팀", request.get("message"));
		    
			//성공 응답
		    response.put("success", true);
		    response.put("message", "데이터가 성공적으로 저장되었습니다.");
		}
		catch(Exception e)
		{
			//실패 응답
			response.put("success", false);
			response.put("error", e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/admin/reports/accept")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Accept_report(@RequestBody Map<String, String> request){
		Map<String, Object> response = new HashMap<>();
		
		try 
		{
			int id = Integer.parseInt(request.get("reportId")); 
			Risk risk = this.riskService.getRisk(id);
			Report report = this.reportService.getReport(id);
			
			//데이터 수정
		    this.riskService.modify(risk, report, risk.getRiskFactor(), risk.getRiskType(), RiskStatus.IN_PROGRESS, risk.getRiskGrade(), report.getReport_managedepartment(), null);
		    
			//성공 응답
		    response.put("success", true);
		    response.put("message", "데이터가 성공적으로 저장되었습니다.");
		}
		catch(Exception e)
		{
			//실패 응답
			response.put("success", false);
			response.put("error", e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/admin/reports/complete")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Complete_report(@RequestBody Map<String, String> request){
		Map<String, Object> response = new HashMap<>();
		
		try 
		{
			int id = Integer.parseInt(request.get("reportId")); 
			Risk risk = this.riskService.getRisk(id);
			Report report = this.reportService.getReport(id);
			
			//데이터 수정
		    this.riskService.modify(risk, report, risk.getRiskFactor(), risk.getRiskType(), RiskStatus.COMPLETED, risk.getRiskGrade(), report.getReport_managedepartment(), null);
		    
			//성공 응답
		    response.put("success", true);
		    response.put("message", "데이터가 성공적으로 저장되었습니다.");
		}
		catch(Exception e)
		{
			//실패 응답
			response.put("success", false);
			response.put("error", e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/admin/riskfiles/delete") /// 위험성 평가 첨부파일 삭제
	@ResponseBody
	public ResponseEntity<Map<String, Object>> riskAssessment_fileDelete (@RequestBody Map<String, Object> request)
	{
		// 파일 삭제 서비스 호출
	    Map<String, Object> response = new HashMap<>();
	    if(this.riskService.file_delete((int)request.get("id")))
	    	response.put("success", true);	    	
	    else
	    	response.put("success", false);
	    
		return ResponseEntity.ok(response); // json 응답 반환
	}
	
 	
}
