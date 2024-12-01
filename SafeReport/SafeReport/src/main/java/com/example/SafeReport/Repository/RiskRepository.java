package com.example.SafeReport.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SafeReport.Entity.Risk;

@Repository
public interface RiskRepository extends JpaRepository<Risk, Integer> {

}
