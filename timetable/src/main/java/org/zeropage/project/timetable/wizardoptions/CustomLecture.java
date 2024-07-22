package org.zeropage.project.timetable.wizardoptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.zeropage.project.timetable.domain.lecture.CustomLectureEntity;
import org.zeropage.project.timetable.domain.lecture.EnrolledLecture;

import java.util.List;

/**
 * Same with Entity version, but not on database.
 * Uses for adding personal schedules on wizard.
 * Have to change this to Entity if timetable is saved.
 */
@Data
@AllArgsConstructor
public class CustomLecture implements Lecture{
    private String name;
    private List<Integer> classHours;

    /**
     * Get credit of subject(lecture). Lecture is customed, so it returns 0.
     */
    public float getCredit(){
        return 0F;
    }

    /**
     * If two lectures are all enrolled by school and name is same,
     * then two lectures are same,
     * which means student cannot register both lectures at once.
     *
     * Uses to avoid two same lectures located at one timetable.
     *
     * Since this is customed, it always returns false.
     */
    public boolean equalsByNameAndType(Lecture lecture){
        return false;
    }

    /**
     * Changes this to Entity version.
     * It doesn't save Entity. Saving process is necessary.
     */
    public CustomLectureEntity toEntity(){
        return new CustomLectureEntity(null, name, classHours);
    }
}
