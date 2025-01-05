package com.example.SafeReport.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.SafeReport.DataNotFoundException;
import com.example.SafeReport.Entity.Users;
import com.example.SafeReport.Repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final, @Notnull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노ㄴ테이션
public class UserService {
	private final UsersRepository userRepository;
	
	public Users getUser(String userid) {
        Optional<Users> Users = userRepository.findByUserid(userid);
        if (Users.isPresent()) {
            return Users.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }
}
