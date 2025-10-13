package com.ats.project.repository;

import com.ats.project.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursesRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
    List<Course> findAll();

}
