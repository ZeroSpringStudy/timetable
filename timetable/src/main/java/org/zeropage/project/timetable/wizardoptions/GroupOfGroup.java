package org.zeropage.project.timetable.wizardoptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupOfGroup {
    private String name;
    private List<LectureGroup> lectureGroups = new ArrayList<>();
    private int numOfGroups;
    /**
     * Uses to see all last priority in one timetable, to decrease meaningless result.
     * Low priority. 후순위 구현 대상.
     */
    private boolean isLastPriority;

    public GroupOfGroup() {
        numOfGroups = 1;
        isLastPriority = false;
    }
    public void addGroup(LectureGroup group){
        lectureGroups.add(group);
    }

    public void deleteGroup(LectureGroup group){
        lectureGroups.remove(group);
    }
}
