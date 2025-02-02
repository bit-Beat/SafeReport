package com.example.SafeReport.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingController {
	@GetMapping("/user/myalert")
	public String myalerts()
	{
		return "setting/alert";
	}
}
