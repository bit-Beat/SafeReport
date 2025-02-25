package com.example.SafeReport.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SafeReport.Entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
	Optional<Users> findByUserid(String userid);
}
