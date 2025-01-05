package com.example.SafeReport;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SafeReportApplicationTests {

	
	@Test
	void testJpa() {
		 // 현재 연도를 가져옵니다.
        int currentYear = LocalDate.now().getYear();

        // LocalDateTime으로 시작 시간 생성
        LocalDateTime startOfYear = LocalDateTime.of(currentYear, 1, 1, 0, 0);

        // 디버깅 출력
        System.out.println("Start of the Year: " + startOfYear);
        System.out.println(LocalDate.now().getMonth());
	}
	

}
