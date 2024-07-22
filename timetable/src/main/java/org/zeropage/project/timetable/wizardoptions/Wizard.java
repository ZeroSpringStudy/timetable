package org.zeropage.project.timetable.wizardoptions;

import org.zeropage.project.timetable.domain.TimetableEntity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Wizard {
    private List<GroupOfGroup> groups = new ArrayList<>();

    /**
     * The day of week that lecture must not be exist.
     */
    private List<DayOfWeek> emptyLecturesList = new ArrayList<>();

    /**
     * The number day of week that lecture must not be exist.
     * Uses if user doesn't want to come school for all weekdays, but do not mind when it is.
     */
    private int numOfLeastEmptyDay;

    /**
     * Start time of lecture.
     * Lecture starts before this time will not be selected.
     */
    private LocalDateTime lectureStartTime;

    /**
     * End time of lecture.
     * Lecture ends after this time will not be selected.
     */
    private LocalDateTime lectureEndTime;

    /**
     * Exclude saturday from empty day limitations.
     * If user wants to register saturday lectures, then user have to set it false.
     */
    private boolean calculateSaturday;

    /**
     * Exclude video lecture from all limitations.
     * For example, video lecture can placed in an empty day,
     * or earlier than lecture start time.
     * Can be set to false if user wants to handle exam schedule.
     */
    private boolean excludeVideoLecture;

    /**
     * Exclude live video lecture only from empty options.
     * For example, live video lecture can placed in an empty day,
     * but not earlier than lecture start time.
     */
    private boolean excludeLivevideoLectureOnEmpty;

    /**
     * Exclude live video lecture from all limitations.
     * For example, live video lecture can placed in an empty day,
     * or earlier than lecture start time.
     */
    private boolean excludeLivevideoLectureAll;

    /**
     * Uses to see all last priority in one timetable, to decrease meaningless result.
     * Low priority. 후순위 구현 대상.
     */
    private boolean showAllLastPriority;

    /**
     * Adds time user want to avoid, both because of other schedule or just they don't want it.
     * Gets CustomLecture, not just time.
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

    public void deleteGroup(GroupOfGroup group){
        groups.remove(group);
    }

    /**
     * Calculate possible timetables with options.
     * @return result of wizard
     */
    public List<TimetableEntity> result(){
        //TODO
        throw new UnsupportedOperationException(); //temporal exception
    }
}
