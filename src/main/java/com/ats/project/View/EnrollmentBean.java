package com.ats.project.View;

import com.ats.project.exceptions.EnrollmentAlreadyExistsException;
import com.ats.project.exceptions.EnrollmentValidationException;
import com.ats.project.model.*;
import com.ats.project.repository.EnrollmentRepo;
import com.ats.project.service.CoursesService;
import com.ats.project.service.EnrollmentService;
import com.ats.project.service.StudentsService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Named("enrollmentBean")
@ViewScoped
@Getter
@Setter
@NoArgsConstructor
public class EnrollmentBean implements Serializable {

    private List<Enrollment> enrollments;
    private List<Semester> semesters;
    private List<Grade> grades;
    private List<EnrollmentStatus> enrollmentStatuses;
    private List <SelectItem> statusSelections;
    private List <SelectItem> activeSelections;
    private Enrollment selectedEnrollment;

    private Long studentId;
    private Long courseId;
    private Semester selectedSemester;
    private String activeFilter;

    @Autowired
    private StudentsService studentsService;
    @Autowired
    private CoursesService coursesService;
    @Autowired
    private EnrollmentRepo enrollmentRepo;
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentBean(StudentsService studentsService, CoursesService coursesService,
                          EnrollmentRepo enrollmentRepo, EnrollmentService enrollmentService) {
        this.studentsService = studentsService;
        this.coursesService = coursesService;
        this.enrollmentRepo = enrollmentRepo;
        this.enrollmentService = enrollmentService;
    }

    @PostConstruct
    public void init() {
        try {
            resetForm();
            refreshEnrollments();
            semesters = List.of(Semester.values());
            grades = List.of(Grade.values());
            enrollmentStatuses = List.of(EnrollmentStatus.values());

            statusSelections = enrollmentStatuses.stream()
                    .map(s -> new SelectItem(s, s.name()))
                    .collect(Collectors.toList());
            statusSelections.add(new SelectItem("", "All Statuses"));
            activeSelections =  List.of(
                    new SelectItem("", "All"),
                    new SelectItem("true", "Active"),
                    new SelectItem("false", "Inactive")
            );

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Error initializing enrollment bean: " + e.getMessage());
        }
    }

    public void resetForm() {
        selectedEnrollment = new Enrollment();
        selectedEnrollment.setId(new EnrollmentId());
        selectedEnrollment.setEnrollmentDate(LocalDate.now());
        selectedEnrollment.setActive(true);
        selectedEnrollment.setStatus(EnrollmentStatus.ENROLLED);
        courseId = null;
        studentId = null;
        selectedSemester = null;
    }

    public void refreshEnrollments() {
        enrollments = enrollmentService.findAll();
    }

    public void setRelations() {
        try {
            if (studentId == null || courseId == null) {
                throw new EnrollmentValidationException("Student ID and Course ID are required");
            }

            var student = studentsService.findById(studentId);
            var course = coursesService.findById(courseId);

            if (student.isEmpty()) {
                throw new EnrollmentValidationException("Student not found with ID: " + studentId);
            }
            if (course.isEmpty()) {
                throw new EnrollmentValidationException("Course not found with ID: " + courseId);
            }

            selectedEnrollment.setStudent(student.get());
            selectedEnrollment.setCourse(course.get());
        } catch (Exception e) {
            addErrorMessage("Error setting relations: " + e.getMessage());
            throw e;
        }
    }

    public void saveEnrollment() {
        try {
            // Validate required fields
            if (selectedSemester == null) {
                addErrorMessage("Please select a semester");
                return;
            }

            // Set relations first
            setRelations();

            // Create enrollment ID
            EnrollmentId enrollmentId = new EnrollmentId();
            enrollmentId.setStudentId(studentId);
            enrollmentId.setCourseId(courseId);
            enrollmentId.setSemester(selectedSemester);

            selectedEnrollment.setId(enrollmentId);

            // Check if this is an update or new enrollment
            if (enrollmentRepo.existsById(enrollmentId)) {
                enrollmentService.updateEnrollment(selectedEnrollment);
                addSuccessMessage("Enrollment updated successfully");
            } else {
                enrollmentService.addEnrollment(selectedEnrollment);
                addSuccessMessage("Enrollment added successfully");
            }

            refreshEnrollments();
            resetForm();

        } catch (EnrollmentAlreadyExistsException e) {
            addErrorMessage("Enrollment already exists: " + e.getMessage());
        } catch (EnrollmentValidationException e) {
            addErrorMessage("Validation error: " + e.getMessage());
        } catch (Exception e) {
            addErrorMessage("Error saving enrollment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteEnrollment(Enrollment enrollment) {
        try {
            enrollmentService.deleteEnrollmentById(enrollment.getId());
            refreshEnrollments();
            addSuccessMessage("Enrollment deleted successfully");
        } catch (Exception e) {
            addErrorMessage("Error deleting enrollment: " + e.getMessage());
        }
    }

    public void setSelectedEnrollment(Enrollment enrollment) {
        try {
            this.selectedEnrollment = new Enrollment();

            // Copy the enrollment data
            this.selectedEnrollment.setId(enrollment.getId());
            this.selectedEnrollment.setStatus(enrollment.getStatus());
            this.selectedEnrollment.setGrade(enrollment.getGrade());
            this.selectedEnrollment.setEnrollmentDate(enrollment.getEnrollmentDate());
            this.selectedEnrollment.setActive(enrollment.isActive());
            this.selectedEnrollment.setStudent(enrollment.getStudent());
            this.selectedEnrollment.setCourse(enrollment.getCourse());

            // Set form fields
            if (enrollment.getCourse() != null) {
                this.courseId = enrollment.getCourse().getId();
            }
            if (enrollment.getStudent() != null) {
                this.studentId = enrollment.getStudent().getId();
            }
            if (enrollment.getId() != null) {
                this.selectedSemester = enrollment.getId().getSemester();
            }
        } catch (Exception e) {
            addErrorMessage("Error setting enrollment for edit: " + e.getMessage());
        }
    }


    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }

    private String sortField, sortOrder;

    public void sortEnrollments() {
        if (sortField == null  || sortOrder == null) {
            return;
        }

        Comparator <Enrollment> comparator = null;

        switch (sortField.toLowerCase()) {
                case "student.name":
                    comparator = Comparator.comparing(e -> e.getStudent().getName());
                    break;

                case "course.name":
                    comparator = Comparator.comparing(e -> e.getCourse().getName());
                    break;

                case "student.id":
                    comparator = Comparator.comparing(e -> e.getStudent().getId());
                    break;

                case "course.id":
                    comparator = Comparator.comparing(e -> e.getCourse().getId());
                    break;
        }
        
        if (("desc").equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        enrollments.sort(comparator);
    }


    private String globalFilter;
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        if (filter == null) { return true;}

        String filterString = filter.toString().toLowerCase();
        Enrollment enrollment = (Enrollment) value;

        return (enrollment.getStudent().getName() != null && enrollment.getStudent().getName().toLowerCase().contains(filterString)
                || enrollment.getCourse().getName() != null && enrollment.getCourse().getName().toLowerCase().contains(filterString)
                || enrollment.getStudent().getId() != null && enrollment.getStudent().getId().toString().equals(filterString)
                || enrollment.getCourse().getId() != null && enrollment.getCourse().getId().toString().equals(filterString));

    }


}