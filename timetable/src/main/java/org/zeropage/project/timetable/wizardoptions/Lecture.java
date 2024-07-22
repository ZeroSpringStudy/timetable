package org.zeropage.project.timetable.wizardoptions;

import java.util.List;

/**
 * Uses to group lecture classes at once.
 */
public interface Lecture {
    String getName();
    List<Integer> getClassHours();

    /**
     * Get credit of subject(lecture). Lecture is customed, so it returns 0.
     */
    float getCredit();

    /**
     * If two lectures are all enrolled by school and name is same,
     * then two lectures are same,
     * which means student cannot register both lectures at once.
     *
     * Uses to avoid two same lectures located at one timetable.
     *
     * Always returns false least one of lecture is customed.
     */
    boolean equalsByNameAndType(Lecture lecture);
}
