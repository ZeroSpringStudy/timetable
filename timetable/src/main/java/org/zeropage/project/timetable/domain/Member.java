package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@ToString //For test. 테스트용.
public class Member implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ID set by user
     * 사용자가 직접 설정한 ID
     */
    @Column(name = "user_name", nullable = false, updatable = false, unique = true, length = userNameLen)
    private String username;

    /**
     * Encoded password
     * 암호화된 비밀번호
     * 가능하면 보안을 위해 getter는 만들지 않을 생각이었으나,
     * BcryptPasswordEncoder 사용을 위해서는 필요해 만들게 됨
     */
    @Column(name = "userpw", nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    @ToString.Exclude //이거 안 하면 서로 참조가 되어 StackOverflow 발생
    private List<Timetable> timetables = new ArrayList<>();

    @Transient
    public static final int userNameLen = 64;

    public Member(String userName, String userPW) {
        this.username = userName;
        this.password = userPW;
    }

    public void addTimeTable(Timetable timetable) {
        this.timetables.add(timetable);
        timetable.setMember(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }
}
