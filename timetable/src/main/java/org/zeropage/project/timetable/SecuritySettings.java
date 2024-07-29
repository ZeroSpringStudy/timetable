package org.zeropage.project.timetable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 보안 관련 사항 리스트
 * 아직 접근제한은 걸지 않았고 비밀번호 암호화 클래스만 존재
 */
@Configuration
public class SecuritySettings {


    /**
     * 비밀번호 암호화 클래스 Bean에 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
