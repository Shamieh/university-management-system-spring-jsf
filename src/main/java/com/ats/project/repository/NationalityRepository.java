package com.ats.project.repository;

import com.ats.project.model.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Long> {
    Optional<Nationality> findByName(String name);
    List<Nationality> findAll();
}
