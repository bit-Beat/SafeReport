package com.example.SafeReport.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SafeReport.DTO.PinPointReportDTO;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Service.ReportService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PinPointController {
	
	private final ReportService reportService;
	
	@GetMapping("/pinpoint/main")
	public String pinpoint_main(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="area", required=false) String area)
	{
		Page<Report> report = this.reportService.getArea_List(page-1, area);  // page - 1로 0부터 시작

		model.addAttribute("page", "pinpoint");
		//model.addAttribute("reportList", report);
		return "/PinPoint/MainPinpoint";
	}
	
	@GetMapping("/admin/reports/by-area")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getReportsByArea( @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="area", required=false) String area) {
		Page<Report> reports = reportService.getArea_List(page-1, area); // 서비스에서 구역별 리스트 반환
		
		// Page를 DTO 리스트로 변환
	    List<PinPointReportDTO> reportDTOs = reports.getContent()
	        .stream()
	        .map(PinPointReportDTO::fromEntity)
	        .collect(Collectors.toList());

	    // 응답 데이터 구성
	    Map<String, Object> response = new HashMap<>();
	    response.put("content", reportDTOs);
	    response.put("currentPage", reports.getNumber()+1);
	    response.put("totalPages", reports.getTotalPages());
	    response.put("totalItems", reports.getTotalElements());

	    return ResponseEntity.ok(response);
	}

}
