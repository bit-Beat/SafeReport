package com.example.SafeReport.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SafeReport.Entity.Users;
import com.example.SafeReport.Enum.UserRole;
import com.example.SafeReport.Repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException { // username -> userid
        Optional<Users> _Users = this.userRepository.findByUserid(userid); // 수정된 부분
        if (_Users.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        Users Users = _Users.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(Users.getRole())) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else if ("adminsafety".equals(Users.getRole())) {
        	authorities.add(new SimpleGrantedAuthority(UserRole.ADMINSAFETY.getValue()));
        } else if ("manager".equals(Users.getRole())) {
        	authorities.add(new SimpleGrantedAuthority(UserRole.MANAGER.getValue()));
        }
        else authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        
        return new User(Users.getUserid(), Users.getPassword(), authorities); // 수정된 부분
    }
}





