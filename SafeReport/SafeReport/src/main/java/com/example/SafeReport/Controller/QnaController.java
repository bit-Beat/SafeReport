package com.example.SafeReport.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QnaController {
	
	@GetMapping("/qna") // 문의 경로
    public String home(Model model) {
		model.addAttribute("page", "qna");
        return "qna";  // qna.html을 반환
    }
	
}
