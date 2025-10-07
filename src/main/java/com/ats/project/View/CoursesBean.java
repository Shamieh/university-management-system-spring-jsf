package com.ats.project.View;

import com.ats.project.model.Courses;
import com.ats.project.model.Enrollment;
import com.ats.project.model.Faculty;
import com.ats.project.model.Students;
import com.ats.project.service.CoursesService;
import com.ats.project.service.FacultyService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Named("coursesBean")
@ViewScoped
@Getter
@Setter
public class CoursesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Courses selectedCourse;
    private Long selectedFacultyId;

    private List<Courses> coursesList;
    private List<Faculty> facultyList;

    @Autowired
    private CoursesService courseService;

    @Autowired
    private FacultyService facultyService;

    @PostConstruct
    public void init() {
        selectedCourse = new Courses();
        refreshCourses();
        refreshFaculties();
    }

    // =========================
    // Create Course
    // =========================
    public void createCourse() {
        try {
            if (selectedCourse != null && selectedFacultyId != null) {
                setRelations();
                courseService.CreateCourses(selectedCourse);
                refreshCourses();
                resetForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // Update Course
    // =========================
    public void updateCourse() {
        try {
            if (selectedCourse != null && selectedCourse.getId() != null) {
                setRelations();
                courseService.updateCourses(selectedCourse);
                refreshCourses();
                resetForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // Save Course (Create or Update)
    // =========================
    public void saveCourse() {
        if (selectedCourse == null || selectedCourse.getId() == null) {
            createCourse();
        } else {
            updateCourse();
        }
    }

    // =========================
    // Delete Course
    // =========================
    public void deleteCourse(Courses course) {
        try {
            if (course != null && course.getId() != null) {
                courseService.deleteById(course.getId());
                refreshCourses();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // Set relations
    // =========================
    private void setRelations() {
        if (selectedFacultyId != null) {
            selectedCourse.setFaculty(facultyService.findById(selectedFacultyId).get());
        }
    }

    // =========================
    // Refresh lists
    // =========================
    private void refreshCourses() {
        coursesList = courseService.findAll();
    }

    private void refreshFaculties() {
        facultyList = facultyService.findAll();
    }

    // =========================
    // Reset form
    // =========================
    public void resetForm() {
        selectedCourse = new Courses();
        selectedFacultyId = null;
    }


    public void setSelectedCourse(Courses course) {
        this.selectedCourse = course;
        if (course != null && course.getFaculty() != null) {
            this.selectedFacultyId = course.getFaculty().getId();
        } else {
            this.selectedFacultyId = null;
        }
    }



    private String sortField, sortOrder;

    public void sortCourse() {
        if (sortField == null  || sortOrder == null) {
            return;
        }

        Comparator<Courses> comparator = null;

        switch (sortField.toLowerCase()) {


            case "course.name":
                comparator = Comparator.comparing(Courses::getName);
                break;


            case "course.id":
                comparator = Comparator.comparing(Courses::getId);
                break;
        }

        if (("desc").equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        coursesList.sort(comparator);
    }



}
