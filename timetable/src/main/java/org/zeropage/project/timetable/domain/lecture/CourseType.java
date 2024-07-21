package org.zeropage.project.timetable.domain.lecture;

public enum CourseType {
    //없음
    NONE,

    //혼합형
    MIXED,

    //동영상
    VIDEO,

    //혼합형(원격70%이상)
    BLENDED,

    //실시간100%
    LIVE_VIDEO;

    public boolean isVideo(){
        return this == VIDEO;
    }

    public boolean isUntact(){
        if (this == VIDEO || this == LIVE_VIDEO) return true;
        else return false;
    }
}