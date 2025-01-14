package com.example.SafeReport.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.SafeReport.DataNotFoundException;
import com.example.SafeReport.Entity.Report;

import com.example.SafeReport.Repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final, @Notnull 이 붙은 필드의 생성자를 자동 새엇ㅇ해주는 롬복 어노ㄴ테이션
public class ReportService {
	
	private final ReportRepository reportRepository;
	
	/// Report Insert
	public Report create(String title, String department, String name, String location, String content, String details, String reporterId)
	{
		Report report = new Report(); // Report 테이블 생성
		
		report.setReport_title(title); // 신고제목
		report.setReport_department(department); // 접수부서
		report.setReport_location(location); // 위치 
		report.setReport_status(""); // 상태값
		report.setReporter_name(name); // 신고자
		report.setReport_content(content); // 신고내용 접수
		report.setReport_detail(details); // 개선내용
		report.setReportcreatedate(LocalDateTime.now()); // 저장시간
		report.setReporter_id(reporterId); // 접수 아이디
		return this.reportRepository.save(report); // 저장 후 리턴
	}
	
	/// Report Update
	public void modify(Report report, String title, String department, String name, String location, String content, String details) {
		
		report.setReport_title(title); // 신고제목
		report.setReport_department(department); // 접수부서
		report.setReport_location(location); // 위치 
		report.setReporter_name(name); // 신고자
		report.setReport_content(content); // 신고내용 접수
		report.setReport_detail(details); // 개선내용
		report.setReportmodifydate(LocalDateTime.now()); // 수정시간
		
        this.reportRepository.save(report); //저장
    }
	
	/// Report Read
	public Report getReport(Integer id) {  
        Optional<Report> report = this.reportRepository.findById(id);
        if (report.isPresent()) {
            return report.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
	
	public void delete(Report report) {
        this.reportRepository.delete(report);
    }
	
	/// 제보 비밀번호 비교 맞으면 true, 틀리면 false 리턴
	/*public boolean reportComparePassword(Report report, String password) {
		if(password.equals(report.getReport_pw()))
			return true;
		else return false;
	}*/
	
}
