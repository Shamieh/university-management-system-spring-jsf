package com.ats.project.service;

import com.ats.project.model.Courses;
import com.ats.project.model.Students;

import java.util.List;
import java.util.Optional;

public interface CoursesService {
    Courses CreateCourses(Courses courses);
    List<Courses> findAll();

    void deleteById(Long id);
    Optional<Courses> findById(Long courseId);

    void updateCourses(Courses courses);
}
