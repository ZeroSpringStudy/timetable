package org.zeropage.project.timetable.repository;

public enum TimeSearchMode {
    /**
     * filter lecture that selected time is all included
     * 설정한 시간이 전부 포함된 강의만 검색
     */
    COVER,

    /**
     * filter lecture that selected time is included least a minute
     * 설정한 시간이 1분이라도 겹치는 강의를 검색
     */
    INCLUDE
}
