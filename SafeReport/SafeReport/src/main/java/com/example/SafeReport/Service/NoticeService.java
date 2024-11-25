package com.example.SafeReport.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.SafeReport.DataNotFoundException;
import com.example.SafeReport.Entity.Notice;
import com.example.SafeReport.Repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {
	
	private final NoticeRepository noticeRepository;
	
	// Notice List Create TOP5 Desc Read
	public List<Notice> getListTop5CreateDateDesc()
	{
		return this.noticeRepository.findTop5ByOrderByCreatedateDesc();
	}
	
	// Notice List All Read
	public List<Notice> getList()
	{
		return this.noticeRepository.findAll();
	}
	
	/// Notice Read
	public Notice getNotice(Integer id) {
		Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
	}
}
