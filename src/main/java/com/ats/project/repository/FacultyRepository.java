package com.ats.project.repository;

import com.ats.project.model.Faculty;
import com.ats.project.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByName(String name);
    List<Faculty> findAll();

    @Query("SELECT s from Student s where s.faculty = :faculty")
    List<Student> findStudentsByFaculty(@Param("faculty") Faculty faculty);

}
