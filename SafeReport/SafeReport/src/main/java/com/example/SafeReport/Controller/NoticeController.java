package com.example.SafeReport.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SafeReport.NoticeForm;
import com.example.SafeReport.Entity.Notice;
import com.example.SafeReport.Service.NoticeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NoticeController {
	private final NoticeService noticeService;
	
	@GetMapping("/notice/list")
	public String noticeList(Model model, @RequestParam(value="page", defaultValue="1") int page) // 모델은 자바와 탬플릿간의 연결고리역할
	{
		List<Notice> activeNotices = noticeService.getActiveNotices(); // active == true인 공지사항
		Page<Notice> paging = this.noticeService.getNoticePage(page-1); // page-1로 0부터 시작

	    // 전체 리스트 결합
	    List<Notice> combinedNotices = new ArrayList<>();
	    combinedNotices.addAll(activeNotices); // 고정 공지사항 먼저 추가
	    combinedNotices.addAll(paging.getContent()); // 일반 공지사항 추가

	    model.addAttribute("combinedNotices", combinedNotices);
	    model.addAttribute("paging", paging); // 페이징 정보를 전달 (이전/다음 버튼에 사용)

		return "board/notice/notice_list";
	}
	
	@GetMapping(value = "/notice/notice_detail/{noticeid}")
	public String noticeDetail(Model model, @PathVariable("noticeid") Integer noticeid) {
		Notice notice = this.noticeService.getNotice(noticeid);
		model.addAttribute("notice", notice);
		return "board/notice/notice_detail";
	}
	
	//수정
	@GetMapping("/notice/update/{noticeid}")
	public String noticeUpdate(NoticeForm noticeForm, @PathVariable("noticeid") Integer noticeid, Principal principal, Model model)
	{
		Notice notice = this.noticeService.getNotice(noticeid);
		
		noticeForm.setNoticeTitle(notice.getTitle());
		noticeForm.setNoticeContent(notice.getContents());
		noticeForm.setActive(notice.getActive());
		
		return "board/notice/notice";
	}

	//수정
	@PostMapping("/notice/update/{noticeid}")
	public String noticeUpdate(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal, @PathVariable("noticeid") Integer noticeid )
	{
		if(bindingResult.hasErrors())
			return "board/notice/notice";
		
		Notice notice = this.noticeService.getNotice(noticeid);
		this.noticeService.update(notice, noticeForm.getNoticeTitle(), noticeForm.getNoticeContent(), noticeForm.getActive());
		
		return "redirect:/notice/notice_detail/{noticeid}";
	}
	
	@GetMapping("/notice")
	public String noticeCreate(NoticeForm noticeForm, Model model, Principal principal)
	{
		
		return "board/notice/notice";
	}
	
	@PostMapping("/notice")
	public String createNotice(@Valid NoticeForm noticeForm, BindingResult bindingResult, Model model, Principal principal)
	{
		if(bindingResult.hasErrors())
			return "board/notice/notice";
		
		this.noticeService.create(
				noticeForm.getNoticeTitle(),
				noticeForm.getNoticeContent(),
				principal.getName(), // 유저명
				noticeForm.getActive() // 상단고정여부
		);
		
		return "redirect:/notice?success";
	}
	

}
