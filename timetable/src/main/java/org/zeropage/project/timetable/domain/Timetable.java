package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@ToString //For test. 테스트용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    private List<LectureTimetable> lectures;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /**
     * Key that cannot change but can see. Uses when user shares their timetable.
     * 변경은 불가하나 볼 수는 있는 key. 시간표를 공유할 때 사용.
     */
    private String viewOnlyKey;

    public Timetable(List<LectureTimetable> lectures, Member member) {
        this.lectures = lectures;
        this.member = member;
    }

    public void addLectureTimetable(LectureTimetable lectureTimeTable){
        // this.lectures.add(lectureTimeTable);
        // 반대쪽에서 여기로 설정을 만지므로 여기서 하면 오히려 중복임
        lectureTimeTable.setTimetable(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getTimetables().add(this);
    }
}
