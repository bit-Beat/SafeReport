package com.example.SafeReport.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SafeReport.Entity.RiskFactor;

@Repository
public interface RiskFactorRepository extends JpaRepository<RiskFactor, Integer> {

}
