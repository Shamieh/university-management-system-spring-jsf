package com.ats.project.view;

import com.ats.project.model.*;
import com.ats.project.repository.CoursesRepository;
import com.ats.project.repository.UserRepo;
import com.ats.project.service.EnrollmentService;
import com.ats.project.service.StudentsService;
import com.ats.project.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Named("studentDashboardBean")
@ViewScoped
@Getter
@Setter
public class StudentDashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private StudentsService studentsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CoursesRepository coursesRepository;

    private Student student;
    private List<Enrollment> activeEnrollments;
    private List<Enrollment> completedEnrollments;
    private List<Enrollment> droppedEnrollments;
    private List<Enrollment> failedEnrollments;

    private double gpa;
    private int currentCreditHours;
    private int completedCreditHours;

    private Long selectedCourseId;
    private Semester selectedSemester;
    private List<Semester> semesters;

    @PostConstruct
    public void init() {
        String username = userService.getLoggedInUsername();
        if (username == null) return;

        this.student = userRepo.findByUsername(username).get().getStudent();
        this.semesters = Arrays.asList(Semester.values());
        loadEnrollmentsAndStats();
    }

    public void loadEnrollmentsAndStats() {
        activeEnrollments = enrollmentService.getEnrollmentsByStudentAndStatus(student.getId(), EnrollmentStatus.ENROLLED);
        completedEnrollments = enrollmentService.getEnrollmentsByStudentAndStatus(student.getId(), EnrollmentStatus.COMPLETED);
        droppedEnrollments = enrollmentService.getEnrollmentsByStudentAndStatus(student.getId(), EnrollmentStatus.DROPPED);
        failedEnrollments = enrollmentService.getEnrollmentsByStudentAndStatus(student.getId(), EnrollmentStatus.FAILED);

        completedCreditHours = completedEnrollments.stream().mapToInt(e -> e.getCourse().getCreditHours()).sum();
        currentCreditHours = activeEnrollments.stream().mapToInt(e -> e.getCourse().getCreditHours()).sum();
        gpa = completedCreditHours == 0 ? 0.0 : student.getGpa();
    }

    public void dropEnrollment(Enrollment enrollment) {
        try {
            enrollmentService.dropEnrollment(enrollment.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Course dropped."));
            loadEnrollmentsAndStats();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Drop failed: " + ex.getMessage(), null));
        }
    }

    public void addEnrollment() {
        try {
            if (selectedCourseId == null || selectedSemester == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select course and semester.", null));
                return;
            }

            Enrollment enrollment = new Enrollment();
            EnrollmentId id = new EnrollmentId(student.getId(), selectedCourseId, selectedSemester);
            enrollment.setId(id);
            enrollment.setStudent(student);
            enrollment.setCourse(coursesRepository.findById(selectedCourseId).orElseThrow());
            enrollment.setStatus(EnrollmentStatus.ENROLLED);
            enrollment.setActive(true);
            enrollment.setEnrollmentDate(LocalDate.now());

            enrollmentService.addEnrollment(enrollment);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Enrolled successfully."));
            loadEnrollmentsAndStats();
            selectedCourseId = null;
            selectedSemester = null;
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enrollment failed: " + ex.getMessage(), null));
        }
    }
}
