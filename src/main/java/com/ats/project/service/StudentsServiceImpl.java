package com.ats.project.service;

import com.ats.project.model.*;
import com.ats.project.repository.EnrollmentRepo;
import com.ats.project.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class StudentsServiceImpl implements StudentsService {

    private final StudentsRepository studentsRepository;
    private final EnrollmentRepo enrollmentRepo;


    @Autowired
    public StudentsServiceImpl(StudentsRepository studentsRepository,  EnrollmentRepo enrollmentRepo) {

        this.studentsRepository = studentsRepository;
        this.enrollmentRepo = enrollmentRepo;
    }

    @Override
    @Transactional
    public Student createStudent(Student student) {
        if (student.getEmail() == null) {
            throw new IllegalArgumentException("Email is required for student registration");
        }

        if (studentsRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalStateException("A student with this email already exists!");
        }

        return studentsRepository.save(student); // always insert
    }


    public Optional<Student> findById(Long id) {
        return studentsRepository.findById(id);
    }


    @Override
    public List<Student> findAll() {
        return studentsRepository.findAll();
    }


    @Override
    @Transactional
    public Student updateStudent(Student updatedData) throws Exception {
        return studentsRepository.findById(updatedData.getId()).map(existing -> {
            existing.setName(updatedData.getName());
            existing.setBirthDate(updatedData.getBirthDate());
            existing.setEmail(updatedData.getEmail());
            existing.setMobileNo(updatedData.getMobileNo());
            existing.setGender(updatedData.getGender());
            existing.setMajor(updatedData.getMajor());
            existing.setFaculty(updatedData.getFaculty());
            existing.setNationality(updatedData.getNationality());
            existing.setSchoolAvg(updatedData.getSchoolAvg());
            existing.setNationalId(updatedData.getNationalId());
            existing.setCompletedCourses(updatedData.getCompletedCourses());
//            calculateGpa(existing);
            return studentsRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Student with ID " + updatedData.getId() + " not found"));


    }

    public Student deleteStudent(Long id) throws Exception {
        Optional<Student> studentOpt = studentsRepository.findById(id);
        if (studentOpt.isPresent()) {
            studentsRepository.deleteById(id);
            System.out.println("Student with ID " + id + " deleted.");
            return studentOpt.get();
        } else {
            throw new RuntimeException("Cannot delete. Student with ID " + id + " does not exist.");
        }
    }



    @Override
    @Transactional
    public Double calculateGpa(Student student) {
        int creditHours=0;
        double gpa = 0;

        for(Course course: student.getCompletedCourses()){

//            List <Enrollment> allCourseEnrollments = enrollmentService.findByStudentAmdCourseId(student.getId(), course.getId());
            List <Enrollment> allCourseEnrollments = enrollmentRepo.findByCourseIdAndStudentId(course.getId(), student.getId());
            Optional<Enrollment> bestEnrollment = allCourseEnrollments.stream()
                    .filter(enrollment -> enrollment.getGrade() != null)
                    .max(Comparator.comparing(enrollment1 -> enrollment1.getGrade().getPoints()));
            if (allCourseEnrollments.isEmpty()) {
                System.out.println("Warning: No enrollments found for student " + student.getId()
                        + " in course " + course.getId() + " (" + course.getName() + ")");
                continue; // Skip this course
            }
            if (bestEnrollment.isPresent()){

            double points = bestEnrollment.get().getGrade().getPoints();
            gpa += points * course.getCreditHours();
            creditHours += course.getCreditHours();
            }
        }

        if(creditHours==0){
            System.out.println("No credit hours found.");
            return 0.0;
        }
        student.setGpa(gpa/creditHours);
        studentsRepository.save(student);
        return gpa/creditHours;
    }

    @Override
    public void addCompletedCourses(Student student) {
        List <Enrollment> studentEnrollments = enrollmentRepo.findAll();

        studentEnrollments.stream()
                .filter(enrollment -> student.getId().equals(enrollment.getStudent().getId()) && enrollment.getStatus() == EnrollmentStatus.COMPLETED)
                .forEach(enrollment -> {
                    student.getCompletedCourses().add(enrollment.getCourse());
                });

        try {
            updateStudent( student);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}