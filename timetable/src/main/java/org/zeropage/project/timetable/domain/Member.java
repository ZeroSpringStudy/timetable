package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    private List<Timetable> timetables = new ArrayList<>();

    public void addTimeTable(Timetable timetable){
        this.timetables.add(timetable);
        timetable.setMember(this);
    }

    public List<Timetable> getTimetables() {
        return timetables;
    }
}
