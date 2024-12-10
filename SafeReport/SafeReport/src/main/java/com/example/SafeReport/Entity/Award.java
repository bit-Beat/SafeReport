package com.example.SafeReport.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary Key 자동 생성
    private Integer awardId;

    @Column(nullable = false)
    private LocalDate awardDate; // 수상 날짜 (년-월 관리)

    @Column(nullable = false, length = 50)
    private String awardType; // 수상 종류 (최우수상, 우수상, 장려상)

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false) // Report와 외래키 관계 설정
    private Report report; // 해당 수상과 연결된 제보

}
