package org.zeropage.project.timetable.wizardoptions;

import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.domain.lecture.CustomLecture;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Wizard {
    private List<GroupOfGroup> groups = new ArrayList<>();

    /**
     * The day of week that lecture must not be exist.
     * 공강 요일 설정.
     */
    private List<DayOfWeek> emptyLecturesList = new ArrayList<>();

    /**
     * The number day of week that lecture must not be exist.
     * Uses if user doesn't want to come school for all weekdays, but do not mind when it is.
     * 공강 일수 설정.
     * 매일 학교 오기는 싫지만 안 오는 날이 언제인지는 상관없을 때 사용.
     */
    private int numOfLeastEmptyDay;

    /**
     * Start time of lecture.
     * Lecture starts before this time will not be selected.
     * 강의시작시각.
     * 이 시간 이전에 시작하는 강의는 선택되지 않음.
     */
    private LocalDateTime lectureStartTime;

    /**
     * End time of lecture.
     * Lecture ends after this time will not be selected.
     * 강의종료시각.
     * 이 시간 이후에 끝나는 강의는 선택되지 않음.
     */
    private LocalDateTime lectureEndTime;

    /**
     * Exclude saturday from empty day limitations.
     * If user wants to register saturday lectures, then user have to set it false.
     * 공강 계산 시 토요일을 제외함.
     * 토요일 강의 수강을 희망 시 false로 설정함.
     */
    private boolean calculateSaturday;

    /**
     * Exclude video lecture from all limitations.
     * For example, video lecture can placed in an empty day,
     * or earlier than lecture start time.
     * Can be set to false if user wants to handle exam schedule.
     * 영상강의를 모든 제한조건에서 제외함.
     * 예를 들어, 공강날에 영상강의가 들어가거나 강의시작시각 이전에 들어갈 수 있음.
     * 시험 일정을 조절하고 싶다면 false로 설정하면 됨.
     */
    private boolean excludeVideoLecture;

    /**
     * Exclude live video lecture only from empty options.
     * For example, live video lecture can placed in an empty day,
     * but not earlier than lecture start time.
     * 실시간 비대면 강의를 공강 조건에서만 제외함.
     * 예를 들어, 공강날에 실시간 비대면 강의는 들어갈 수 있으나,
     * 강의시작시간 이전에는 들어갈 수 없음.
     */
    private boolean excludeLivevideoLectureOnEmpty;

    /**
     * Exclude live video lecture from all limitations.
     * For example, live video lecture can placed in an empty day,
     * or earlier than lecture start time.
     * 실시간 비대면 강의를 모든 조건에서 제외함.
     * 예를 들어, 공강날에 실시간 비대면 강의가 들어가거나 강의시작시각 이전에 들어갈 수 있음.
     */
    private boolean excludeLivevideoLectureAll;

    /**
     * Uses to see all last priority in one timetable, to decrease meaningless result.
     * 의미없는 결과를 줄이기 위해 마지막 우선순위를 한번에 시간표에 표시할 때 사용.
     * Low priority. 후순위 구현 대상.
     */
    private boolean showAllLastPriority;

    /**
     * Adds time user want to avoid, both because of other schedule or just they don't want it.
     * Gets CustomLecture, not just time.
     * 다른 일정의 문제 또는 단순히 원하지 않아 피하고 싶은 시간대를 추가함.
     * 단순히 시간을 받지 않고 CustomLecture를 받음.
     * @param lecture
     */
    public void addTimeToAvoid(CustomLecture lecture){
        LectureGroup lectureGroup = new LectureGroup();
        lectureGroup.setName(lecture.getName());
        lectureGroup.addLecture(lecture);
        lectureGroup.setNumOfLecturePicks(1);
        GroupOfGroup groupOfGroup = new GroupOfGroup();
        groupOfGroup.setName(lecture.getName());
        groupOfGroup.addGroup(lectureGroup);
        groupOfGroup.setLastPriority(false);
        this.addGroup(groupOfGroup);
    }

    public void addGroup(GroupOfGroup group){
        groups.add(group);
    }

    public void addGroup(){
        GroupOfGroup group = new GroupOfGroup();
        group.addGroup(new LectureGroup());
        groups.add(group);
    }

    public void deleteGroup(GroupOfGroup group){
        groups.remove(group);
    }

    /**
     * Calculate possible timetables with options.
     * 설정을 기반으로 가능한 시간표를 만듦.
     * @return result of wizard 마법사 결과
     */
    public List<Timetable> result(){
        //TODO
        throw new UnsupportedOperationException(); //temporal exception 임시 예외
    }
}
