package org.zeropage.project.timetable.domain.lecture;

public enum CourseType {
    /**
     * 없음
     */
    NONE,

    /**
     * 혼합형(원격70%이상)+대면
     */
    BLENDED,

    /**
     * 실시간100%
     */
    //
    LIVE_VIDEO,

    /**
     * 혼합형(실시간+동영상)
     */
    MIXED,

    /**
     * 동영상
     */
    VIDEO;

    public boolean isVideo(){
        return this == VIDEO;
    }

    public boolean isUntact(){
        if (this == LIVE_VIDEO || this == MIXED || this == VIDEO) return true;
        else return false;
    }
}