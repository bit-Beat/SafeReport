package com.example.SafeReport;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 이 파일이 스프링의 환경 설정 파일임을 의미하는 애너테이션
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만든다. 이 애너테이션을 사용하면 스프링 시큐리티를 활성화하는 역할을 한다. URL에 sECURITYfILTERcHAIN 클래스가 동작하여 모든 URL에 이 클래스가 필터로 적용되어 URL별로 특별한 설정이 가능하다.
public class SecurityConfig { // Spring Security 설정을 담당
	 @Bean
	    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http
	        	//.csrf(csrf -> csrf.disable()) // CSRF 비활성화 (람다 방식 사용), 실제서비스에서는 반드시 죽일 것!
	            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
	                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
	            .formLogin((formLogin) -> formLogin
	                .loginPage("/user/login")
	                .usernameParameter("userid") // 로그인 시 username -> userid로 변경
	                .defaultSuccessUrl("/home")) // 로그인 성공 시 기본 페이지로 리다이렉트 
	            .logout((logout) -> logout
	            	.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
	                .logoutSuccessUrl("/home")
	                .invalidateHttpSession(true))
	        ;
	        return http.build();
	    }
	 
	 @Bean
	 PasswordEncoder passwordEncoder() {
		 //return new BCryptPasswordEncoder(); // BCrypt 암호화 방식으로 패스워드 암호화
		 return NoOpPasswordEncoder.getInstance(); // 암호화 비교 없이 원문 그대로 비교 
	 }
	
	@Bean  //AuthenticationManager 빈을 생성, 스프링 시큐리티의 인증을 처리한다. 사용자 인증 시 앞서 작성한 usersecurityservice와 passwordEncoder를 내부적으로 사용하여 인증과 권한 부여 프로세스를 처리한다.
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}

}

// 빈이란? 빈(Bean)은 스프링에 의해 생성 또는 관리되는 객체를 의미
// 우리가 지금껏 만들어 왔던 컨트롤러, 서비스, 리포지터리 등도 모두 빈에 해당
// 앞선 예시처럼 @Bean 애너테이션을 통해 자바 코드 내에서 별도로 빈을 정의하고 등록할 수 있다.