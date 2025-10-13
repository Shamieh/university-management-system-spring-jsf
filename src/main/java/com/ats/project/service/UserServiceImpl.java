package com.ats.project.service;

import com.ats.project.model.Student;
import com.ats.project.model.User;
import com.ats.project.repository.StudentsRepository;
import com.ats.project.repository.UserRepo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final StudentsRepository studentsRepository;
    PasswordEncoder passwordEncoder;
    UserRepo userRepo;


    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepo userRepo, StudentsRepository studentsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.studentsRepository = studentsRepository;
    }

    @Override
    @Transactional
    public void createUser(String username, String rawPassword, String role, Long studentId) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        if (studentId != null) {
            Student s = studentsRepository.findById(studentId).orElse(null);
            user.setStudent(s);
        }

        userRepo.save(user);
    }



    @Override
    public void linkUserToStudent(String username, Long studentId) {
        User user = userRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        Student student = studentsRepository.findById(studentId).orElseThrow(()-> new RuntimeException("Student not found"));

        user.setStudent(student);
        userRepo.save(user);
    }

    @Override
    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }



    @Override
    public Student getLoggedInStudent() {
        String username = getLoggedInUsername();
        if (username == null) return null;

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found for current session"));

        return user.getStudent();
    }
}
