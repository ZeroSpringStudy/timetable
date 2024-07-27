package com.example.demo.domain.lecture;

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

    /**
     * Not for user to use. Only for staff.
     * 관리용. 사용자가 사용하라고 만든 것이 아님.
     */
    public static Classification parse(String classificationStr) {
        Classification result;
        switch (classificationStr) {
            case "교양" -> result = GENERAL_EDUCATION;
            case "전공기초" -> result = BASIC_MAJOR;
            case "전공" -> result = MAJOR;
            case "전공필수" -> result = REQUIRED_MAJOR;
            case "전공선택" -> result = ELECTIVE_MAJOR;
            case "교직" -> result = TEACHER_CERTIFICATE;
            case "교직전공" -> result = TEACHING_MAJOR;
            case "자유선택" -> result = ELECTIVE;
            default -> throw new IllegalStateException(classificationStr + "은 올바른 이수구분이 아닙니다.");
        }
        return result;
    }

    public boolean isMajor(){
        if (this == MAJOR ||
                this == REQUIRED_MAJOR ||
                this == ELECTIVE_MAJOR ||
                this == TEACHING_MAJOR)
            return true;
        else return false;
    }
}