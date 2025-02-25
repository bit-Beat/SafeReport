package com.example.SafeReport.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class RiskCategory {
	@Id
	@Column(length = 50)
	private String category; // 위험요인 분류
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<RiskFactor> riskFactor = new ArrayList<>();
}
