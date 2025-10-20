package com.ats.project.service;


import com.ats.project.model.Enrollment;
import com.ats.project.model.EnrollmentId;
import com.ats.project.model.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {

    List<Enrollment> findAll();
    long count();
    public Page<Enrollment> getEnrollments(int page, int size);
    public Page<Enrollment> getEnrollments(int page, int size, String sortField, Sort.Direction direction, Map<String, Object> filters);
    Enrollment findById(EnrollmentId id);
    List<Enrollment> findByCourseId(Long id);
    List<Enrollment> findByStudentId(Long id);
    void addEnrollment(Enrollment enrollment);
    void deleteEnrollmentById(EnrollmentId id);
     List<Enrollment> findByStudentAmdCourseId(Long studentId, Long courseId);
     void updateEnrollment(Enrollment updatedEnrollment);
     void dropEnrollment(EnrollmentId id);
    List<Enrollment> getEnrollmentsByStudentAndStatus(Long studentId, EnrollmentStatus status);
    public List<Enrollment> getAllFilteredEnrollments(Map<String, Object> filters, String sortField, Sort.Direction direction);
}

