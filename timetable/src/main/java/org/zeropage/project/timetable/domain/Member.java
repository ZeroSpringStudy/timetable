package org.zeropage.project.timetable.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.ToString;

@Entity
@ToString //For test. 테스트용.
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ID set by user
     * 사용자가 직접 설정한 ID
     */
    @Column(nullable = false, updatable = false, unique = true, length = 64)
    private String userName;

    /**
     * Encoded password
     * 암호화된 비밀번호
     */
    @Column(nullable = false)
    private String password;
}
