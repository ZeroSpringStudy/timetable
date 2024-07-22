package org.zeropage.project.timetable.domain.lecture;

public enum Classification {
    /**
     * 교양
     */
    GENERAL_EDUCATION,

    /**
     * 전공기초
     */
    BASIC_MAJOR,

    /**
     * 전공
     */
    MAJOR,

    /**
     * 전공필수
     */
    REQUIRED_MAJOR,

    /**
     * 전공선택
     */
    ELECTIVE_MAJOR,

    /**
     * 교직
     */
    TEACHER_CERTIFICATE,

    /**
     * 교직전공
     */
    TEACHING_MAJOR,

    /**
     * 자유선택
     */
    ELECTIVE;

    public boolean isMajor(){
        if (this == MAJOR ||
                this == REQUIRED_MAJOR ||
                this == ELECTIVE_MAJOR ||
                this == TEACHING_MAJOR)
            return true;
        else return false;
    }
}