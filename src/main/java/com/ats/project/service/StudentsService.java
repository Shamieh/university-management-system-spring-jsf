package com.ats.project.service;

import com.ats.project.model.Students;

import java.util.List;
import java.util.Optional;

public interface StudentsService {
    Students createStudent(Students student);
    Optional<Students> findById(Long id);

    List<Students> findAll();

    Students updateStudent( Students updatedData) throws Exception;

    Students deleteStudent(Long id) throws Exception;

    Double calculateGpa(Students student);

    void addCompletedCourses(Students student);



}