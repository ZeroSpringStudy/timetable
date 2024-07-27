package com.example.demo.domain.lecture;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("N")
public class NewLecture extends Lecture{
    private String etc;
}
