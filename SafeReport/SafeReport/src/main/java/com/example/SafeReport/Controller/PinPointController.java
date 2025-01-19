package com.example.SafeReport.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PinPointController {
	
	@GetMapping("/pinpoint/main")
	public String pinpoint_main(Model model)
	{
		model.addAttribute("page", "pinpoint");
		return "/PinPoint/MainPinpoint";
	}

}
