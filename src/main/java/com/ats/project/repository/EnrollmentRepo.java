package com.ats.project.repository;

import com.ats.project.model.Enrollment;
import com.ats.project.model.EnrollmentId;
import com.ats.project.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepo extends JpaRepository <Enrollment, EnrollmentId> {
    @Query("SELECT e from Enrollment e where e.course.id= :courseId AND e.student.id = :studentId AND e.status = 'COMPLETED' ")
    List<Enrollment> findCompletedByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Query("SELECT e from Enrollment e where e.course.id= :courseId AND e.student.id = :studentId")
    List<Enrollment> findByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Query("SELECT e from Enrollment e where e.course.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT e from Enrollment e where e.student.id = :studentId")
    List<Enrollment> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT e from Enrollment e where e.student.id = :studentId AND e.status = 'COMPLETED'")
    List<Enrollment> findCompletedByStudentId(@Param("studentId") Long studentId);


    List<Enrollment> findEnrollmentsByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
}
