package com.example.SafeReport.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.SafeReport.Entity.RiskCategory;

@Repository
public interface RiskCategoryRepository extends JpaRepository<RiskCategory, String> {

}
