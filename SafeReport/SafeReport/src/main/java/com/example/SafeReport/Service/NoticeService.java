package com.example.SafeReport.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	public List<Notice> findTop5ByActiveFalseOrderByCreatedateDesc()
	{
		return this.noticeRepository.findTop5ByActiveFalseOrderByCreatedateDesc();
	}
	
	// Notice Page Read Where : Active = true Order by Createdate DESC
	public List<Notice> getActiveNotices() {
		return noticeRepository.findByActiveTrueOrderByCreatedateDesc();
	}
	
	// Notice List All Read
	public List<Notice> getList()
	{
		return this.noticeRepository.findAll();
	}
	
	// Notice Page Read Where : Active = false Order by Createdate DESC
	// Notice Page Read
	public Page<Notice> getNoticePage(int page)
	{
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdate"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        return this.noticeRepository.findByActiveFalse(pageable);
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
	
	/// Notice Insert
	public void create(String title, String contents, String author, Boolean active)
	{
		Notice notice = new Notice(); // Notice 테이블 생성
		
		notice.setTitle(title); // 제목
		notice.setContents(contents); // 내용
		notice.setAuthor(author); // 작성자
		notice.setCreatedate(LocalDateTime.now()); // 생성시간
		notice.setViewcount(0);
		notice.setActive(active ); // 상단고정여부

		this.noticeRepository.save(notice); // 저장
	}
	
	/// Notice Update
	public void update(Notice notice, String title, String contents, Boolean active)
	{
		notice.setTitle(title);
		notice.setContents(contents);
		notice.setActive(active);
		notice.setUpdatedate(LocalDateTime.now());
		
		this.noticeRepository.save(notice);
	}
		
}
