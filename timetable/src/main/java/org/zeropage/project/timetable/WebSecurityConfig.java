package org.zeropage.project.timetable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 보안 관련 사항 리스트
 * 아직 접근제한은 걸지 않았고 비밀번호 암호화 클래스만 존재
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests
                            .requestMatchers("/saved/user/**").hasRole("USER")
                            .requestMatchers("/result/save/**").hasRole("USER")
                            .anyRequest().permitAll();
                })
                .formLogin((formLogin) -> {
                    formLogin.usernameParameter("userName")
                            .passwordParameter("userPW")
                            .defaultSuccessUrl("/", true);
                })
                .logout((logout) -> {
                    logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true);
                });
        return http.build();
    }

    /**
     * 비밀번호 암호화 클래스 Bean에 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
