package com.ats.project.service;

import com.ats.project.model.Student;
import com.ats.project.model.User;

public interface UserService {
    public void createUser(String username, String rawPassword, String role, Long studentId);
    public void linkUserToStudent(String username, Long studentId);
    public Student getLoggedInStudent();
    public String getLoggedInUsername();
}
