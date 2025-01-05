package com.example.SafeReport.Entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Users {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userid; // 유저 id
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;
    
    @Column(nullable = false)
    @ColumnDefault("'user'") // default
    private String role; // 권한
	
}
