package com.ats.project.model;

import com.ats.project.model.Courses;
import com.ats.project.model.Students;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enrollment")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;


    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Students student;


    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Courses course;

//    @Enumerated(EnumType.STRING)
//    @MapsId("semester")
//    @JoinColumn(name = "semester")
//    private Semester semester;

    private boolean isActive;

    private Grade grade;

    LocalDate enrollmentDate;

    public Enrollment(Students student, Courses course, boolean isActive, Grade grade,
                      Semester semester, EnrollmentStatus status) {

        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }


        this.student = student;
        this.course = course;

        this.isActive = isActive;
        this.grade = grade;
        this.status = status;
        this.enrollmentDate = LocalDate.now();
        this.id = new EnrollmentId();
        this.id.setSemester(semester);
    }


    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    @Transient
    public String getDisplayId() {
        if (id == null) return "";
        return "S" + id.getStudentId() +
                " | C" + id.getCourseId() +
                " |" + (id.getSemester() != null ? id.getSemester().name() : "");
    }




}
