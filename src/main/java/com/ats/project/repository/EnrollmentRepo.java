package com.ats.project.repository;

import com.ats.project.model.Enrollment;
import com.ats.project.model.EnrollmentId;
import com.ats.project.model.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepo extends JpaRepository <Enrollment, EnrollmentId> {

    @Override
    Page<Enrollment> findAll(Pageable pageable);

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

    // FIXED: Changed LOWER to CAST for ID fields (they are Long type, not String)
    @Query("SELECT e FROM Enrollment e " +
            "WHERE LOWER(e.student.name) LIKE %:search% " +
            "OR CAST(e.student.id AS string) LIKE %:search% " +
            "OR LOWER(e.course.name) LIKE %:search% " +
            "OR CAST(e.course.id AS string) LIKE %:search%")
    Page<Enrollment> findByGlobalSearch(@Param("search") String search, Pageable pageable);

    // FIXED: Changed LOWER to CAST for ID fields (they are Long type, not String)
    @Query("SELECT e FROM Enrollment e " +
            "WHERE LOWER(e.student.name) LIKE %:search% " +
            "OR CAST(e.student.id AS string) LIKE %:search% " +
            "OR LOWER(e.course.name) LIKE %:search% " +
            "OR CAST(e.course.id AS string) LIKE %:search%")
    List<Enrollment> findByGlobalSearch(@Param("search") String search, Sort sort);

    List<Enrollment> findEnrollmentsByStudentIdAndStatus(Long studentId, EnrollmentStatus status);

    // FIXED: Added query for filtering by status (for dropdown filter)
    @Query("SELECT e FROM Enrollment e WHERE e.status = :status")
    Page<Enrollment> findByStatus(@Param("status") EnrollmentStatus status, Pageable pageable);

    // FIXED: Added query for filtering by active status (for dropdown filter)
    @Query("SELECT e FROM Enrollment e WHERE e.isActive = :active")
    Page<Enrollment> findByActive(@Param("active") boolean active, Pageable pageable);
    @Query("SELECT e FROM Enrollment e WHERE " +
            "(:status IS NULL OR e.status = :status) AND " +
            "(:active IS NULL OR e.isActive = :active)")
    Page<Enrollment> findAllWithFilters(@Param("status") EnrollmentStatus status,
                                        @Param("active") Boolean active,
                                        Pageable pageable);

}