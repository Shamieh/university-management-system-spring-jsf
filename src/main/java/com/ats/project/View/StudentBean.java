package com.ats.project.View;

import com.ats.project.model.Faculty;
import com.ats.project.model.Major;
import com.ats.project.model.Nationality;
import com.ats.project.model.Students;
import com.ats.project.service.FacultyService;
import com.ats.project.service.MajorService;
import com.ats.project.service.NationalityService;
import com.ats.project.service.StudentsService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Named("studentBean")
@ViewScoped
@Getter
@Setter
@NoArgsConstructor

public class StudentBean implements Serializable {


    @Autowired
    private StudentsService studentsService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private NationalityService nationalityService;
    @Autowired
    private FacultyService facultyService;

    Students  student;
    private Long facultyId;
    private Long majorId;
    private Long nationalityId;


    private List<Students> students;
    private List<Faculty> faculties ;
    private List<Major> majors;
    private List<Nationality> nationalities;

    private Students selectedStudent;


    public StudentBean(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @PostConstruct
    public void init (){
        student =  new Students();
        refreshStudents();
        refreshFaculties();
        refreshMajors();
        refreshNationalities();
    }

    public void refreshStudents(){
        students = studentsService.findAll();
    }

    public void refreshFaculties(){
        faculties = facultyService.findAll();
    }

    public void refreshMajors(){
        majors = majorService.findAll();
    }

    public void refreshNationalities(){
        nationalities = nationalityService.findAll();
    }

    public void deleteStudent(Students student){
        try {
            if(student != null){
            studentsService.deleteStudent(student.getId());
            refreshStudents();

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

//    public void updateStudent(){
//        try {
//            setRelations();
//            studentsService.updateStudent(selectedStudent);
//            refreshStudents();
//            resetForm();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public void addStudent(){
//        try{
//                setRelations();
//                studentsService.createStudent(selectedStudent);
//                refreshStudents();
//                resetForm();
//
//        } catch (RuntimeException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void saveStudent() {
        try {
            if (selectedStudent.getId() == null) {
                // New student
                setRelations();
                studentsService.createStudent(selectedStudent);
            } else {
                // Existing student
                setRelations();
                studentsService.updateStudent(selectedStudent);
            }
            refreshStudents();
            resetForm();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void setSelectedStudent(Students student) {
        this.selectedStudent = student;
        this.facultyId = student.getFaculty().getId();
        this.majorId = student.getMajor().getId();
        this.nationalityId = student.getNationality().getId();
    }

    public void resetForm (){
        selectedStudent = new Students();
        facultyId = null;
        majorId = null;
        nationalityId = null;
    }

    private void setRelations() {

        if(facultyId != null)
            selectedStudent.setFaculty(facultyService.findById(facultyId).get());

        if(nationalityId != null)
            selectedStudent.setNationality(nationalityService.findById(nationalityId));

        if(majorId != null)
            selectedStudent.setMajor(majorService.findById(majorId));
        }

    private String sortField, sortOrder;

    public void sortStudents() {
        if (sortField == null  || sortOrder == null) {
            return;
        }

        Comparator<Students> comparator = null;

        switch (sortField.toLowerCase()) {
            case "student.name":
                comparator = Comparator.comparing(Students::getName);
                break;


            case "student.id":
                comparator = Comparator.comparing(Students::getId);
                break;

        }

        if (("desc").equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        students.sort(comparator);
    }



}
