package com.example.SafeReport.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.SafeReport.DataNotFoundException;
import com.example.SafeReport.DTO.RiskAssessmentDTO;
import com.example.SafeReport.Entity.Report;
import com.example.SafeReport.Entity.Risk;
import com.example.SafeReport.Entity.RiskAssessmentA;
import com.example.SafeReport.Entity.RiskAssessmentB;
import com.example.SafeReport.Entity.RiskAssessmentC;
import com.example.SafeReport.Entity.RiskAssessmentFile;
import com.example.SafeReport.Enum.RiskGrade;
import com.example.SafeReport.Enum.RiskStatus;
import com.example.SafeReport.Repository.ReportRepository;
import com.example.SafeReport.Repository.RiskAssessmentARepository;
import com.example.SafeReport.Repository.RiskAssessmentBRepository;
import com.example.SafeReport.Repository.RiskAssessmentCRepository;
import com.example.SafeReport.Repository.RiskAssessmentFileRepository;
import com.example.SafeReport.Repository.RiskRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RiskService {
	private final RiskRepository riskRepository;
	private final ReportRepository reportRepository;
	
	private final RiskAssessmentFileRepository riskAssessmentFileRepository; // 위험성 평가 첨부파일
	private final RiskAssessmentARepository riskAssessmentARepository; // A등급 위험성평가
	private final RiskAssessmentBRepository riskAssessmentBRepository; // B등급 위험성평가
	private final RiskAssessmentCRepository riskAssessmentCRepository; // C등급 위험성평가
	
	@Value("${file.upload-dir}") // application.properties에서 값 읽기
	private String uploadDir;
	
	
	/// Report Read
	public Risk getRisk(Integer id) {  
	    Optional<Risk> risk = this.riskRepository.findByReportid(id);
	    
	    if (risk.isPresent()) {
	        return risk.get();
	    } else {
	        throw new DataNotFoundException("question not found");
	    }
	}
	
	
	/// RiskB-Reportid로 조회
	public Optional<RiskAssessmentB> getRiskB_Reportid(Report report)
	{
		return this.riskAssessmentBRepository.findByReportid(report);

	}
	
	public void creteRisk_FkReport(Report report)
	{
		Risk risk = new Risk(); // risk테이블생성 
		risk.setReportid(report); // fk연결
		risk.setStatus(RiskStatus.PENDING);
		risk.setRiskGrade(RiskGrade.UNDEFINED);
		
        this.riskRepository.save(risk);
	}
	
	// Find Reports with Risks by Keyword(title, reporter_name), Status, RiskGrade
	public Page<Report> getFindRisks(String keyword, RiskStatus status, RiskGrade riskGrade, String manageDepartment, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("reportcreatedate"));
		Pageable pageable = PageRequest.of(page, 100, Sort.by(sorts));
	    return riskRepository.findByKeywordAndFilters(keyword, status, riskGrade, manageDepartment, pageable);
	}
	
	/// Report Select Year and Month 
	public Page<Report> getReportsByYearAndMonth(Integer year, Integer month, String keyword, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("reportcreatedate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return riskRepository.findByYearAndMonth(year, month, keyword, pageable);
    }
	
	/// 관리자 수상내역관리에 제보 목록 리스트 찾기
	public void getFindRisksToAwards(String keyword, Integer year, Integer month, int page) {
	    Pageable pageable = PageRequest.of(page, 10); // 페이지 크기 10

	}

	/// Risk Update
	public void modify(Risk risk, Report report, String riskFactor, String riskType, RiskStatus status, RiskGrade riskGrade, String manageDepartment, String rejectMessage) {
		
		risk.setRiskFactor(riskFactor); // 위험요인 (ex. 전기, 화재, 기계 등)
		risk.setRiskType(riskType); // 위험유형 (ex. 화재, 충돌, 낙상 등)
		risk.setStatus(status); // 상태
		risk.setRiskGrade(riskGrade); //위험등금 enum(a, b, c, d)
		risk.setLastModifiedDate(LocalDateTime.now()); // 수정시간
		risk.setRejectMessage(rejectMessage); // 반려사유
		
		report.setReport_managedepartment(manageDepartment); // 제보 테이블에 담당부서 변경
		
        this.riskRepository.save(risk);
        this.reportRepository.save(report);
    }
	
	public RiskAssessmentA saveOrUpdateRiskAssessmentA(Report report, int possibilityBefore, int possibilityAfter, int importanceBefore, int importanceAfter, String workername, String supervisor, String representative
                                                   , Boolean essentialActive, Boolean administrativeActive, Boolean engineeringActive, Boolean equipmentActive
                                                   , String essentialmeasures, String administrativemeasures, String engineeringmesaures, String personalequipment
                                                   , String confirmedmeasured, LocalDate confirmedDate, MultipartFile[] files) {

		// 기존 데이터 조회 (Report ID 기반)
		Optional<RiskAssessmentA> existingAssessment = riskAssessmentARepository.findByReportid(report);
		RiskAssessmentA riskAssessmentA;

	    if (existingAssessment.isPresent()) { // 기존 데이터 업데이트
	        riskAssessmentA = existingAssessment.get();
	        riskAssessmentA.setPossibilityBefore(possibilityBefore);
	        riskAssessmentA.setPossibilityAfter(possibilityAfter);
	        riskAssessmentA.setImportanceBefore(importanceBefore);
	        riskAssessmentA.setImportanceAfter(importanceAfter);
	        riskAssessmentA.setWorkerName(workername);
	        riskAssessmentA.setSupervisorName(supervisor);
	        riskAssessmentA.setRepresentativeName(representative);
	        riskAssessmentA.setEssential_checked(essentialActive);
	        riskAssessmentA.setAdministrative_checked(administrativeActive);
	        riskAssessmentA.setEngineering_checked(engineeringActive);
	        riskAssessmentA.setEquipment_checked(equipmentActive);
	        riskAssessmentA.setEssential_measures(essentialmeasures);
	        riskAssessmentA.setAdministrative_measures(administrativemeasures);
	        riskAssessmentA.setEngineering_measures(engineeringmesaures);
	        riskAssessmentA.setPersonal_equipment(personalequipment);
	        riskAssessmentA.setConfirmed_measured(confirmedmeasured);
	        riskAssessmentA.setConfirmed_date(confirmedDate);
	        riskAssessmentA.setModifiedDate(LocalDateTime.now());
	    } else { // 새로운 데이터 생성
	        riskAssessmentA = new RiskAssessmentA();
	        riskAssessmentA.setReportid(report);
	        riskAssessmentA.setPossibilityBefore(possibilityBefore);
	        riskAssessmentA.setPossibilityAfter(possibilityAfter);
	        riskAssessmentA.setImportanceBefore(importanceBefore);
	        riskAssessmentA.setImportanceAfter(importanceAfter);
	        riskAssessmentA.setWorkerName(workername);
	        riskAssessmentA.setSupervisorName(supervisor);
	        riskAssessmentA.setRepresentativeName(representative);
	        riskAssessmentA.setEssential_checked(essentialActive);
	        riskAssessmentA.setAdministrative_checked(administrativeActive);
	        riskAssessmentA.setEngineering_checked(engineeringActive);
	        riskAssessmentA.setEquipment_checked(equipmentActive);
	        riskAssessmentA.setEssential_measures(essentialmeasures);
	        riskAssessmentA.setAdministrative_measures(administrativemeasures);
	        riskAssessmentA.setEngineering_measures(engineeringmesaures);
	        riskAssessmentA.setPersonal_equipment(personalequipment);
	        riskAssessmentA.setConfirmed_measured(confirmedmeasured);
	        riskAssessmentA.setConfirmed_date(confirmedDate);
	        riskAssessmentA.setCreatedate(LocalDateTime.now());
	    }
	
	    // **파일 업로드 로직 (여러 개 파일 지원)**
	    if (files != null) { // ✅ 파일이 존재하는 경우에만 실행
	        for (MultipartFile file : files) {
	            if (file != null && !file.isEmpty()) {
	                try {
	                    String uuid = UUID.randomUUID().toString();
	                    String fileName = file.getOriginalFilename();
	                    String extension = "";
	
	                    int dotIndex = fileName.lastIndexOf(".");
	                    if (dotIndex > 0) extension = fileName.substring(dotIndex);
	
	                    String uploadFileName = uuid + extension;
	                    String filePath = uploadDir + "riskFile/" + uploadFileName;
	
	                    // 디렉토리 생성
	                    File directory = new File(uploadDir + "riskFile/");
	                    if (!directory.exists()) directory.mkdirs();
	
	                    file.transferTo(new File(filePath)); // 파일 저장
	
	                    // 파일 정보 저장
	                    RiskAssessmentFile riskFile = new RiskAssessmentFile();
	                    riskFile.setFileName(fileName);
	                    riskFile.setFileUploadName(uploadFileName);
	                    riskFile.setFilePath(filePath);
	                    riskFile.setFileType(extension);
	                    riskFile.setFileSize(file.getSize());
	                    riskFile.setUploadedBy("TEST");
	                    riskFile.setUploadDate(LocalDateTime.now());
	                    riskFile.setAssessmentType("A");
	                    riskFile.setReportid(report);
	
	                    riskAssessmentFileRepository.save(riskFile);
	                } catch (Exception e) {
	                    throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
	                }
	            }
	        }
	    }
	    return riskAssessmentARepository.save(riskAssessmentA);
	}

	/// RiskAssessement B Merge
	public RiskAssessmentB saveOrUpdateRiskAssessmentB(Report report, String possibilityBefore, String possibilityAfter, String workername, String supervisor, String representative
													,Boolean essentialActive, Boolean administrativeActive, Boolean engineeringActive, Boolean equipmentActive 
													,String essentialmeasures, String administrativemeasures, String engineeringmesaures, String personalequipment
													,String confirmedmeasured, LocalDate confirmedDate) {
        
        // ReportId로 기존 데이터 조회
        Optional<RiskAssessmentB> existingAssessment = riskAssessmentBRepository.findByReportid(report);

        RiskAssessmentB riskAssessmentB;
        if (existingAssessment.isPresent()) { // 데이터가 있으면 업데이트
            riskAssessmentB = existingAssessment.get();
            riskAssessmentB.setPossibilityBefore(possibilityBefore);
            riskAssessmentB.setPossibilityAfter(possibilityAfter);
            riskAssessmentB.setWorkerName(workername);
            riskAssessmentB.setSupervisorName(supervisor);
            riskAssessmentB.setRepresentativeName(representative);
            riskAssessmentB.setEssential_checked(essentialActive); // 본질적대책 Y/N
            riskAssessmentB.setAdministrative_checked(administrativeActive); // 공학적대책 Y/N
            riskAssessmentB.setEngineering_checked(engineeringActive); // 관리적대책 Y/N
            riskAssessmentB.setEquipment_checked(equipmentActive); // 개인보호구 Y/N
            riskAssessmentB.setEssential_measures(essentialmeasures); // 본질적대책
            riskAssessmentB.setAdministrative_measures(administrativemeasures); // 공학적대책
            riskAssessmentB.setEngineering_measures(engineeringmesaures); // 관리적대책
            riskAssessmentB.setPersonal_equipment(personalequipment); // 개인보호구
            riskAssessmentB.setConfirmed_measured(confirmedmeasured); // 확정대책
            riskAssessmentB.setConfirmed_date(confirmedDate); // 조치예정일
                        
            riskAssessmentB.setModifiedDate(LocalDateTime.now()); // 수정일
        } else { // 데이터가 없으면 새로 삽입
            riskAssessmentB = new RiskAssessmentB();            
            riskAssessmentB.setReportid(report);
            riskAssessmentB.setPossibilityBefore(possibilityBefore);
            riskAssessmentB.setPossibilityAfter(possibilityAfter);
            riskAssessmentB.setWorkerName(workername);
            riskAssessmentB.setSupervisorName(supervisor);
            riskAssessmentB.setRepresentativeName(representative);
            riskAssessmentB.setEssential_checked(essentialActive); // 본질적대책 Y/N
            riskAssessmentB.setAdministrative_checked(administrativeActive); // 공학적대책 Y/N
            riskAssessmentB.setEngineering_checked(engineeringActive); // 관리적대책 Y/N
            riskAssessmentB.setEquipment_checked(equipmentActive); // 개인보호구 Y/N
            riskAssessmentB.setEssential_measures(essentialmeasures); // 본질적대책
            riskAssessmentB.setAdministrative_measures(administrativemeasures); // 공학적대책
            riskAssessmentB.setEngineering_measures(engineeringmesaures); // 관리적대책
            riskAssessmentB.setPersonal_equipment(personalequipment); // 개인보호구
            riskAssessmentB.setConfirmed_measured(confirmedmeasured); // 확정대책
            riskAssessmentB.setConfirmed_date(confirmedDate); // 조치예정일
            
            riskAssessmentB.setCreatedate(LocalDateTime.now()); //생성일
        }

        // 저장
        return riskAssessmentBRepository.save(riskAssessmentB);
    }
	
	public void RiskC_save(RiskAssessmentDTO request, Report report, Integer no)
	{
		RiskAssessmentC riskAssessment = new RiskAssessmentC();
        riskAssessment.setCategory(request.getCategory()); // 구분
        riskAssessment.setContents(request.getContents()); // 내용
        riskAssessment.setResult(request.getResult()); // 위험성 확인결과
        riskAssessment.setImprovement(request.getImprovement()); // 개선내용
        riskAssessment.setNo(no);
        riskAssessment.setReportid(report);
        
        riskAssessmentCRepository.save(riskAssessment);

	}
	
	public void RiskC_DeleteReport(Report report)
	{
		riskAssessmentCRepository.deleteByReport(report); // report_id에 해당하는 Award 삭제
	}
	
	public boolean file_delete(Integer id)
	{
		Optional<RiskAssessmentFile> optionalRiskFile = riskAssessmentFileRepository.findById(id);
		RiskAssessmentFile riskFile;
		String storedFileName = "";

		if (optionalRiskFile.isPresent()) {
		    riskFile = optionalRiskFile.get(); // 값 가져오기
		    storedFileName = riskFile.getFileUploadName(); // 고유 파일명 가져오기
		} else {
		    throw new RuntimeException("해당 파일 ID에 대한 정보를 찾을 수 없습니다: " + id);
		}
 
	    // 파일명이 일치하는지 확인
	    if (storedFileName != null && !storedFileName.isEmpty()) {
	        File file = new File(uploadDir + "riskFile/" + storedFileName); // 서버에서 파일 삭제
	        if (file.exists()) {
	            boolean isDeleted = file.delete(); // 파일 삭제
	            if (!isDeleted) throw new RuntimeException("파일 삭제 실패: " + storedFileName);
	        }

	        // 데이터베이스에서 파일 정보 삭제
	        this.riskAssessmentFileRepository.delete(riskFile);
	        return true;
	    } else {
	    	throw new RuntimeException("삭제할 파일이 존재하지 않습니다.");
	    }
	}
	
	public void riskFile_Insert(MultipartFile file, Report report, String assessmentType) throws IOException {
	    if (file.isEmpty()) return;

	    String uuid = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String extension = "";

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) extension = fileName.substring(dotIndex);

        String uploadFileName = uuid + extension;
        String filePath = uploadDir + "riskFile/" + uploadFileName;

        // 디렉토리 생성
        File directory = new File(uploadDir + "riskFile/");
        if (!directory.exists()) directory.mkdirs();

        file.transferTo(new File(filePath)); // 파일 저장

        // 파일 정보 저장
        RiskAssessmentFile riskFile = new RiskAssessmentFile();
        riskFile.setFileName(fileName);
        riskFile.setFileUploadName(uploadFileName);
        riskFile.setFilePath(filePath);
        riskFile.setFileType(extension);
        riskFile.setFileSize(file.getSize());
        riskFile.setUploadedBy("TEST");
        riskFile.setUploadDate(LocalDateTime.now());
        riskFile.setAssessmentType(assessmentType); // 'A', 'B', 'C' 중 하나 저장
        riskFile.setReportid(report);

        riskAssessmentFileRepository.save(riskFile);
	}

}
