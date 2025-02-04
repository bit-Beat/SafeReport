package com.example.SafeReport.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.SafeReport.DataNotFoundException;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Repository.ReportRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final, @Notnull 이 붙은 필드의 생성자를 자동 새엇ㅇ해주는 롬복 어노ㄴ테이션
public class ReportService {
	
	@Value("${file.upload-dir}") // application.properties에서 값 읽기
	private String uploadDir;
	 
	private final ReportRepository reportRepository;
	
	/// Report Insert
	@Transactional // 트랜잭션 시작, 하나 실패 시 모두 rollback
	public Report create(String title, String department, String name, String location, String content, String details, String reporterId, MultipartFile file)
	{
		String fileName = "";
        String filePath = "";
        String extension = ""; // 파일 확장자 추출
        String uploadFileName = "";// 고유 파일명, 서버에 보내기위한 고유한 파일명

        try { // 파일 업로드
            if (file != null && !file.isEmpty()) {
            	String uuid = UUID.randomUUID().toString(); // 고유한 UUID 생성
                
            	fileName = file.getOriginalFilename();

                int dotIndex = fileName.lastIndexOf(".");
                if (dotIndex > 0) extension = fileName.substring(dotIndex); // 확장자 포함
                
                uploadFileName = uuid + extension; // UUID + 확장자 형태의 고유 파일명 반환
                filePath = uploadDir + uploadFileName;

                // 디렉토리 생성
                File directory = new File(uploadDir);
                if (!directory.exists()) directory.mkdirs();

                file.transferTo(new File(filePath)); // 파일 저장
            }
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다: " + e.getMessage(), e);  // 파일 업로드 실패 시 예외 처리
        }

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
        report.setReport_managedepartment("안전환경팀"); // 관리부서
        report.setAttachfile(fileName); // 파일명 저장
        report.setAttachfile_upload(uploadFileName); // 고유값 파일명
        return this.reportRepository.save(report); // 저장 후 리턴
	}
	
	/// Report Update
	public void modify(Report report, String title, String department, String name, String location, String content, String details, MultipartFile file) {
		String fileName = "";
        String filePath = "";
        String extension = ""; // 파일 확장자 추출
        String uploadFileName = "";// 고유 파일명, 서버에 보내기위한 고유한 파일명
        
        
        
		try { // 파일 업로드
			if (file != null && !file.isEmpty()) {
				if(report.getAttachfile()!=null && !report.getAttachfile().isEmpty())
		        	file_delete(report); // 파일 있으면 삭제
				
            	String uuid = UUID.randomUUID().toString(); // 고유한 UUID 생성
                
            	fileName = file.getOriginalFilename();

                int dotIndex = fileName.lastIndexOf(".");
                if (dotIndex > 0) extension = fileName.substring(dotIndex); // 확장자 포함
                
                uploadFileName = uuid + extension; // UUID + 확장자 형태의 고유 파일명 반환
                filePath = uploadDir + uploadFileName;

                // 디렉토리 생성
                File directory = new File(uploadDir);
                if (!directory.exists()) directory.mkdirs();

                file.transferTo(new File(filePath)); // 파일 저장
                report.setAttachfile(fileName); // 파일명 저장
                report.setAttachfile_upload(uploadFileName); // 고유값 파일명
            }
			report.setReport_title(title); // 신고제목
			report.setReport_department(department); // 접수부서
			report.setReport_location(location); // 위치 
			report.setReporter_name(name); // 신고자
			report.setReport_content(content); // 신고내용 접수
			report.setReport_detail(details); // 개선내용
			report.setReportmodifydate(LocalDateTime.now()); // 수정시간
			this.reportRepository.save(report); //저장
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다: " + e.getMessage(), e);  // 파일 업로드 실패 시 예외 처리
        }
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
	
	@Transactional
	public void delete(Report report) {
		String storedFileName = report.getAttachfile_upload(); // 고유 파일명 가져오기
	    if (storedFileName != null && !storedFileName.isEmpty()) {
	        File file = new File(uploadDir + storedFileName);
	        if (file.exists()) {
	            boolean isDeleted = file.delete(); // 파일 삭제
	            if (!isDeleted) throw new RuntimeException("파일 삭제에 실패했습니다: " + storedFileName);
	        }
	    }
        this.reportRepository.delete(report);
    }
	
	public boolean file_delete(Report report)
	{
	    String storedFileName = report.getAttachfile_upload(); // 고유 파일명 가져오기
	    //String fileName = report.getAttachfile(); // 실제 파일명 가져오기

	    // 파일명이 일치하는지 확인
	    if (storedFileName != null && !storedFileName.isEmpty()) {
	        File file = new File(uploadDir + storedFileName); // 서버에서 파일 삭제
	        if (file.exists()) {
	            boolean isDeleted = file.delete(); // 파일 삭제
	            if (!isDeleted) throw new RuntimeException("파일 삭제 실패: " + storedFileName);
	        }

	        // 데이터베이스에서 파일 정보 삭제
	        report.setAttachfile(""); // 원본 파일명 삭제
	        report.setAttachfile_upload(""); // 고유 파일명 삭제
	        this.reportRepository.save(report); // 변경 사항 저장
	        return true;
	    } else {
	    	throw new RuntimeException("삭제할 파일이 존재하지 않습니다.");
	    }
	}
	
	/// 제보 비밀번호 비교 맞으면 true, 틀리면 false 리턴
	/*public boolean reportComparePassword(Report report, String password) {
		if(password.equals(report.getReport_pw()))
			return true;
		else return false;
	}*/
	
}
