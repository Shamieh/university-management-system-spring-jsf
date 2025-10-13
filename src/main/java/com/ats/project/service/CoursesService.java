package com.ats.project.service;

import com.ats.project.model.Course;

import java.util.List;
import java.util.Optional;

public interface CoursesService {
    Course CreateCourses(Course courses);
    List<Course> findAll();

    void deleteById(Long id);
    Optional<Course> findById(Long courseId);

    void updateCourses(Course courses);
}
