package com.example.SafeReport.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	@GetMapping("/user/login")
	public String login()
	{
		return "login_form";
	}
	
	

}
