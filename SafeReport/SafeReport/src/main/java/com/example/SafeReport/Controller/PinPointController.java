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
public class PinPointController {
	
	private final IndexService indexService;
	
	@GetMapping("/pinpoint/main")
	public String pinpoint_main(Model model)
	{
		List<Report> report = this.indexService.getList(); // Report
		
		model.addAttribute("page", "pinpoint");
		model.addAttribute("reportList", report);
		return "/PinPoint/MainPinpoint";
	}

}
