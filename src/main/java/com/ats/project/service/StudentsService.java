package com.ats.project.service;

import com.ats.project.model.Student;

import java.util.List;
import java.util.Optional;


public interface StudentsService {
    Student createStudent(Student student);
    Optional<Student> findById(Long id);

    List<Student> findAll();

    Student updateStudent(Student updatedData) throws Exception;

    Student deleteStudent(Long id) throws Exception;

    Double calculateGpa(Student student);

    void addCompletedCourses(Student student);



}