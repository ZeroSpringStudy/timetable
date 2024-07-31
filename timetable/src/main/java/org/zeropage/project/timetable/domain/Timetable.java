package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.*;
import org.zeropage.project.timetable.domain.lecture.Lecture;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString //For test. 테스트용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "timetable")
    private List<LectureTimetable> lectures;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /**
     * Key that cannot change but can see. Uses when user shares their timetable.
     * 변경은 불가하나 볼 수는 있는 key. 시간표를 공유할 때 사용.
     */
    @Column(unique = true, length = viewOnlyKeyLen)
    private String viewOnlyKey;

    @Transient
    public static final int viewOnlyKeyLen = 64;

    public Timetable(List<LectureTimetable> lectures) {
        this.lectures = lectures;
        this.member = null;
        this.viewOnlyKey = null;
    }

    /**
     * lecture List를 받아 LectureTimetable과 timetable 객체를 생성함
     * numOfLastPriority 인자가 없으면 생성자 중복으로 충돌이 발생해 별도의 인자를 받음
     * @param lectures 강의
     * @param numOfLastPriority 마지막 우선순위의 수
     */
    public Timetable(List<Lecture> lectures, int numOfLastPriority) {
        ArrayList<LectureTimetable> lectureTimetables = new ArrayList<>();
        int lastPriorityStartIdx = lectures.size() - numOfLastPriority;
        for (int i = 0; i < lectures.size(); i++) {
            Lecture lecture = lectures.get(i);
            // 시간 겹치는지, 등록된 강의면 같은 강의인지 확인, 단 마지막 우선순위는 제외
            for (int j = 0; j < lectureTimetables.size() && j < lastPriorityStartIdx; j++) {
                Lecture lectureToCompare = lectureTimetables.get(j).getLecture();
                if (lecture.isClassOverlaped(lectureToCompare)) {
                    throw new IllegalStateException("시간이 겹치는 강의가 있습니다. 다시 확인해 주시기 바랍니다. " +
                            lecture.toString() + ", " + lectureToCompare.toString());
                }
                /* lastPriority 구현되면 그때 추가하는 게 좋을 듯함
                else if (lecture.equals(lectureToCompare)) {
                    throw new IllegalStateException("과목번호 기준으로 강의가 여러 개 있습니다." +
                            " 다시 확인해 주시기 바랍니다. " +
                            lecture.toString() + ", " + lectureToCompare.toString());
                }*/
            }
            lectureTimetables.add(
                    new LectureTimetable(lecture, this, i >= lastPriorityStartIdx));
        }
        this.lectures = lectureTimetables;
        this.member = null;
        this.viewOnlyKey = null;
    }

    public void setViewOnlyKey(String viewOnlyKey) {
        if (this.viewOnlyKey == null)
            this.viewOnlyKey = viewOnlyKey;
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
