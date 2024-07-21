package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
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
     */
    private String viewOnlyKey;

    public TimetableEntity(List<TimetableLecture> lectures, Member member) {
        this.lectures = lectures;
        this.member = member;
    }
}
