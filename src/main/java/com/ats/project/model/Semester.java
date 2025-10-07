package com.ats.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum Semester {


    SPRING_2025(LocalDate.of(2025,3,1), LocalDate.of(2025,6,30), false),
    SUMMER_2025(LocalDate.of(2025,8,1), LocalDate.of(2025,9,30), true),
    FALL_2025(LocalDate.of(2025,11,1), LocalDate.of(2026,1,30), false);


    private final LocalDate startDate;
    private final LocalDate endDate;
    private final boolean isActive;


}
