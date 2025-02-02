package com.example.SafeReport.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SafeReport.Entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, Integer> {

}
