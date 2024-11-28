package com.example.SafeReport.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SafeReport.Entity.Notice;
import com.example.SafeReport.Service.NoticeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NoticeController {
	private final NoticeService noticeService;
	
	@GetMapping("/notice/list")
	public String noticeList(Model model, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value = "keyword", defaultValue = "") String keyword) // 모델은 자바와 탬플릿간의 연결고리역할
	{
		//Page<Report> paging = this.indexService.getList(page-1, keyword);  // page - 1로 0부터 시작
		//model.addAttribute("paging", paging); // 모델 객체에 questionList라는 이름으로 저장했다. 
		//model.addAttribute("keyword", keyword);
		
		List<Notice> notice = this.noticeService.getList();
		model.addAttribute("notice", notice);
		return "board/notice/notice_list";
	}
	
	@GetMapping(value = "/board/notice/notice_detail/{noticeid}")
	public String noticeDetail(Model model, @PathVariable("noticeid") Integer noticeid) {
		Notice notice = this.noticeService.getNotice(noticeid);
		model.addAttribute("notice", notice);
		return "board/notice/notice_detail";
	}
	
	@GetMapping("/notice")
	public String noticeCreate(Model model, Principal principal)
	{
		return "board/notice/notice";
	}
}
