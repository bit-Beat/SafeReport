package com.example.SafeReport.Entity;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Users {
	
    @Id
    @Column(unique = true, nullable = false)
    private String userid; // 유저 id
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;
    
    private String department; // 부서
    
    @Column(nullable = false)
    @ColumnDefault("'user'") // default
    private String role; // 권한
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Alert> alert; // 위험성평가 c
	
    
}
