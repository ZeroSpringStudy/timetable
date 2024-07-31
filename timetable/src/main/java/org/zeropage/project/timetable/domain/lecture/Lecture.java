package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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
     * 일요일에는 강의가 없으므로 생각하지 않았음. (어느 정도 대비를 해 놓긴 함)
     * 별도의 Entity를 쓰면 join 문제가 발생해 String으로 임시 대체
     */
    private String classHours;

    /**
     * 시간 저장 List버전.
     * DB에 저장되지는 않는 Column이고, 화면 표시용으로 사용함
     */
    @Transient
    private List<Integer> classHoursByList = new ArrayList<>();

    @Transient
    private List<LectureStartTimeAndEndTime> classHoursByTimeList = new ArrayList<>();

    @Transient
    private String classHoursByTimeStr;

    public Lecture(String name, String classHours) {
        this.id = null;
        this.name = name;
        this.classHours = classHours;
    }

    public Lecture(String name, List<Integer> classHoursByList) {
        this.name = name;
        this.classHoursByList = classHoursByList;
    }

    /**
     * boolean은 위 생성자와 구분하기 위해 추가함
     */
    public Lecture(String name, List<LectureStartTimeAndEndTime> lectureStartTimeAndEndTimes,
                   boolean configurer) {
        this.name = name;
        this.classHoursByTimeList = lectureStartTimeAndEndTimes;
    }

    /**
     * Get credit of lecture. If lecture is customed, then it returns 0.
     * 강의의 학점을 반환함. 사용자가 직접 만든 강의라면, 0을 반환함.
     */
    public float getCredit(){
        return 0F;
    }

    /**
     * 데이터 활용을 위해 int의 배열로도 바꿔서 저장함(이 데이터는 DB에는 없으므로 여기서 시행)
     * 데이터를 가져올 때마다 실행해야 함
     */
    public void setClassHoursByList() {
        ArrayList<Integer> classHoursByList = new ArrayList<>();
        String[] classHoursByStr = getClassHours().split(",");
        for (String classHour : classHoursByStr) {
            if (classHour.isBlank()) continue;
            classHoursByList.add(Integer.valueOf(classHour));
        }
        //시간 정보가 없으면 빈 리스트 넣어주기까지만(null 들어가는 것 방지)
        //시간 볼 때 예외 발생하면 이거 먼저 setter로 변경할 필요 있음
        this.classHoursByList = classHoursByList;

        ArrayList<Integer> classHours = new ArrayList<>(classHoursByList);
        Collections.sort(classHours);
        //연속되는 것끼리 분리해야 함
        while (classHours.size() != 0) {
            classHoursByTimeList.add(new LectureStartTimeAndEndTime(classHours));
        }
        String classHoursByTimeStr = "";
        for (LectureStartTimeAndEndTime time : classHoursByTimeList) {
            classHoursByTimeStr += time.toString() + ", ";
        }
        classHoursByTimeStr = classHoursByTimeStr.substring(0, classHoursByTimeStr.length() - 2);
        this.classHoursByTimeStr = classHoursByTimeStr;
    }

    /**
     * DB 저장을 위해 int의 배열을 문자열로 변경하는 과정
     * 저장 직전 시행할 필요 있음
     */
    public void setClassHours() {
        if (classHoursByList.size() == 0) {
            ArrayList<Integer> classHoursByInt = new ArrayList<>();
            for (LectureStartTimeAndEndTime time : this.classHoursByTimeList) {
                classHoursByInt.addAll(time.toIntegerArray());
            }
            this.classHoursByList = classHoursByInt;
        }
        String classHours = ",";
        for (Integer classHour : classHoursByList)
            classHours += classHour.toString() + ',';
        this.classHours = classHours;
    }

    public boolean isClassOverlaped(Lecture lecture) {
        ArrayList<Integer> overlapHours = new ArrayList<>(this.getClassHoursByList());
        overlapHours.retainAll(lecture.getClassHoursByList());
        return overlapHours.size() != 0;
    }

    /**
     * RegisteredLecture.equals 메서드 사용을 위해 임시로 만들어 놓은 것
     * 다만 쓸 일 없을듯 함
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecture lecture)) return false;
        if (getId() == null && lecture.getId() == null) return false;
        return Objects.equals(getId(), lecture.getId());
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
