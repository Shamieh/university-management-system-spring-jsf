package com.ats.project.repository;

import com.ats.project.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Long> {
    Optional<Students> findByEmail(String email);
    List<Students> findAll();



}
