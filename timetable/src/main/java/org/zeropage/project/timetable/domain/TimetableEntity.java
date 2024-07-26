package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@ToString //For test. 테스트용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableEntity {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    private List<TimetableLecture> lectures;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /**
     * Key that cannot change but can see. Uses when user shares their timetable.
     * 변경은 불가하나 볼 수는 있는 key. 시간표를 공유할 때 사용.
     */
    private String viewOnlyKey;

    public TimetableEntity(List<TimetableLecture> lectures, Member member) {
        this.lectures = lectures;
        this.member = member;
    }
}
