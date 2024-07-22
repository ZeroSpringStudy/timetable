package org.zeropage.project.timetable.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.ToString;

@Entity
@ToString //For test.
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ID set by user
     */
    @Column(nullable = false, updatable = false, unique = true, length = 64)
    private String userId;

    /**
     * Encoded password
     */
    @Column(nullable = false)
    private String password;
}
