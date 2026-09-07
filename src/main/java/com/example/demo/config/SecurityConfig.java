package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 글쓰기/수정/삭제는 로그인 필요 -> 이걸 먼저 적어야 아래 permitAll에 안 묻힘
                        .requestMatchers("/board/save", "/board/update/**", "/board/delete/**").authenticated()
                        // 목록, 상세보기, 로그인/회원가입 화면, css는 로그인 없이 접근 가능
                        .requestMatchers("/", "/board/**", "/css/**",
                                "/member/join", "/member/login").permitAll()
                        // 나머지는 전부 로그인 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/member/login")           // 우리가 만든 로그인 화면 사용
                        .loginProcessingUrl("/member/login")  // 로그인 폼이 이 주소로 POST를 보냄 (Security가 가로챔)
                        .usernameParameter("memberId")        // 우리 input name="memberId" 랑 맞춤
                        .passwordParameter("memberPass")      // 우리 input name="memberPass" 랑 맞춤
                        .defaultSuccessUrl("/board/", true)   // 로그인 성공하면 목록으로
                        .failureUrl("/member/login?error")    // 로그인 실패하면 에러 표시하며 다시 로그인 화면으로
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout")          // 이 주소로 접근하면 로그아웃 처리
                        .logoutSuccessUrl("/board/")           // 로그아웃 후 목록으로
                )
                // 학습 단계라 CSRF는 잠시 꺼둠 (실제 서비스라면 각 폼에 토큰 넣고 켜야 함 - Step 6 이후 설명)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}