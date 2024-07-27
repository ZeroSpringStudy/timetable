package com.example.demo.domain.lecture;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("R")
public class RegisteredLecture extends Lecture{
    private Integer credit; // 학점
    private Integer grade;  // 학년
    private String degree;  // 과정
    private String community;   // 대학
    private String department;  // 개설학과
    private String lectureType; // 이수구분
    private Integer lectureNum; // 분반
    private String lectureCode; // 과목번호
    private String professor;   // 담당교수
    private String isClosed;   // 폐강
    private String isFlexible; // 유연학기
    private String isVideo; // 수업유형(동영상)
    private String etc; // 비고
}
