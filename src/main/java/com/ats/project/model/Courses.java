package com.ats.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
//@ToString
@Entity
@Table(name="Courses")
@NoArgsConstructor
public class Courses {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer creditHours;

    private Boolean isActive;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    private int maxCapacity;

    private int currentEnrollment;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
                fetch = FetchType.EAGER)//})
    @JoinTable(
            name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private List<Courses> prerequisiteCourses = new ArrayList<>();



    @ManyToMany(mappedBy = "completedCourses")
    private Set<Students> studentsWhoCompleted = new HashSet<>();


    public Courses(String name, Integer creditHours, Boolean isActive, String description, Faculty faculty, int maxCapacity, int currentEnrollment) {
        this.name = name;
        this.creditHours = creditHours;
        this.isActive = isActive;
        this.description = description;
        this.faculty = faculty;
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = currentEnrollment;
    }
}