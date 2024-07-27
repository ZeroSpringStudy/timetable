package org.zeropage.project.timetable.domain.lecture;

public enum CourseType {
    /**
     * 없음
     */
    NONE,

    /**
     * 대면 수업
     */
    OFFLINE,

    /**
     * 혼합형(원격70%이상)+대면수업
     */
    BLENDED,

    /**
     * 실시간100%
     */
    LIVE_VIDEO,

    /**
     * 혼합형(실시간+동영상)
     */
    MIXED,

    /**
     * 동영상
     */
    VIDEO;

    /**
     * Not for user to use. Only for staff.
     * 관리용. 사용자가 사용하라고 만든 것이 아님.
     */
    public static CourseType parse(String courseTypeStr) {
        CourseType result;
        switch (courseTypeStr) {
            case "" -> result = NONE;
            case "대면 수업" -> result = OFFLINE;
            case "혼합형(원격70%이상)+대면수업" -> result = BLENDED;
            case "실시간100%" -> result = LIVE_VIDEO;
            case "혼합형(실시간+동영상)" -> result = MIXED;
            case "동영상" -> result = VIDEO;
            default -> throw new IllegalStateException(courseTypeStr + "은 올바른 수업유형이 아닙니다.");
        }
        return result;
    }

    public boolean isVideo(){
        return this == VIDEO;
    }

    public boolean isUntact(){
        if (this == LIVE_VIDEO || this == MIXED || this == VIDEO) return true;
        else return false;
    }
}