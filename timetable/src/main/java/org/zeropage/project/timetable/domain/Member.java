package org.zeropage.project.timetable.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ID set by user
     */
    private String userId;

    /**
     * Encoded password
     */
    private String password;
}
