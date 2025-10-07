package com.ats.project.service;


import com.ats.project.model.Enrollment;
import com.ats.project.model.EnrollmentId;

import java.util.List;

public interface EnrollmentService {

    List<Enrollment> findAll();
    Enrollment findById(EnrollmentId id);
    List<Enrollment> findByCourseId(Long id);
    List<Enrollment> findByStudentId(Long id);
    void addEnrollment(Enrollment enrollment);
    void deleteEnrollmentById(EnrollmentId id);
     List<Enrollment> findByStudentAmdCourseId(Long studentId, Long courseId);
     void updateEnrollment(Enrollment updatedEnrollment);
}
