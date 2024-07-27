package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString //For test. 테스트용.
public abstract class Lecture {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * Save by splitting one week by 30 minutes. Split by ','.
     * 0 is Monday 00:00~00:30,
     * 1 is Monday 00:30~01:00,
     * 47 is Monday 23:30~24:00,
     * 48 is Tuesday 00:00~00:30,
     * 95 is Tuesday 23:30~24:00,
     * 143 is Wednesday 23:30~24:00,
     * 191 is Thursday 23:30~24:00,
     * 239 is Friday 23:30~24:00,
     * 287 is Saturday 23:30~24:00.
     * Didn't expected Sunday because there are no enrolled lecture on Sunday.
     * 한 주를 30분으로 나누어 저장함. ','으로 구분.
     * 0은 월요일 00:00~00:30,
     * 1은 월요일 00:30~01:00,
     * 47은 월요일 23:30~24:00,
     * 48은 화요일 00:00~00:30,
     * 95는 화요일 23:30~24:00,
     * 143은 수요일 23:30~24:00,
     * 191은 목요일 23:30~24:00,
     * 239는 금요일 23:30~24:00,
     * 287은 토요일 23:30~24:00.
     * 일요일에는 강의가 없으므로 생각하지 않았음.
     * 별도의 Entity를 쓰면 join 문제가 발생해 String으로 임시 대체
     */
    private String classHours;

    /**
     * 시간 저장 List버전.
     * DB에 저장되지는 않는 Column이고, 화면 표시용으로 사용함
     */
    @Transient
    private List<Integer> classHoursByList = new ArrayList<>();

    public Lecture(String name, String classHours) {
        this.id = null;
        this.name = name;
        this.classHours = classHours;
    }


    /**
     * Get credit of lecture. If lecture is customed, then it returns 0.
     * 강의의 학점을 반환함. 사용자가 직접 만든 강의라면, 0을 반환함.
     */
    public float getCredit(){
        return 0F;
    }

    public boolean isClassOverlaped(Lecture lecture){
        ArrayList<Integer> thisHours = new ArrayList<>();
        ArrayList<Integer> otherHours = new ArrayList<>();
        String[] thisStr = this.getClassHours().replace(" ", "").split(",");
        for (String time : thisStr) {
            thisHours.add(Integer.parseInt(time));
        }
        String[] otherStr = lecture.getClassHours().replace(" ", "").split(",");
        for (String time : otherStr) {
            thisHours.add(Integer.parseInt(time));
        }
        thisHours.retainAll(otherHours);
        return thisHours.size() == 0;
    }


    /**
     * If two lectures are all enrolled by school and name is same,
     * then two lectures are same,
     * which means student cannot register both lectures at once.
     *
     * Uses to avoid two same lectures located at one timetable.
     *
     * Always returns false least one of lecture is customed.
     */
    public boolean equalsByNameAndType(Lecture lecture){
        return this instanceof RegisteredLecture &&
                lecture instanceof RegisteredLecture &&
                this.getName().equals(lecture.getName());
    }
}
