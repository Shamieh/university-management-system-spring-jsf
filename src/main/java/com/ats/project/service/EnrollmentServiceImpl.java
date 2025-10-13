package com.ats.project.service;

import com.ats.project.exceptions.EnrollmentAlreadyExistsException;
import com.ats.project.exceptions.EnrollmentValidationException;
import com.ats.project.model.*;
import com.ats.project.repository.CoursesRepository;
import com.ats.project.repository.EnrollmentRepo;
import com.ats.project.repository.StudentsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final CoursesRepository coursesRepository;
    private final StudentsRepository studentsRepository;
    private final StudentsService studentsService;
    EnrollmentRepo enrollmentRepo;

    public EnrollmentServiceImpl(EnrollmentRepo enrollmentRepo, CoursesService coursesService, CoursesRepository coursesRepository, StudentsRepository studentsRepository, StudentsService studentsService) {
        this.enrollmentRepo = enrollmentRepo;
        this.coursesRepository = coursesRepository;
        this.studentsRepository = studentsRepository;
        this.studentsService = studentsService;
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollmentRepo.findAll();
    }

    @Override
    public Enrollment findById(EnrollmentId id) {
        return enrollmentRepo.findById(id).orElse(null);
    }

    @Override
    public List<Enrollment> findByCourseId(Long id) {
        return findAll().stream()
                .filter(e -> e.getCourse().getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> findByStudentId(Long id) {
        return findAll().stream()
                .filter(e -> e.getStudent().getId().equals(id)).collect(Collectors.toList());
    }

    public List<Enrollment> findByStudentAmdCourseId(Long studentId, Long courseId) {
        return findAll().stream()
                .filter(e -> e.getStudent().getId().equals(studentId)
                        && e.getCourse().getId().equals(courseId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateEnrollment(Enrollment updatedEnrollment) {
        Enrollment oldEnrollment = findById(updatedEnrollment.getId());
        oldEnrollment.setStatus(updatedEnrollment.getStatus());
        oldEnrollment.setGrade(updatedEnrollment.getGrade());
        oldEnrollment.setEnrollmentDate(updatedEnrollment.getEnrollmentDate());
        oldEnrollment.setActive(updatedEnrollment.isActive());

        if(oldEnrollment.getStatus().equals(EnrollmentStatus.COMPLETED)) {
            oldEnrollment.getStudent().getCompletedCourses().add(oldEnrollment.getCourse());
            studentsRepository.save(oldEnrollment.getStudent());
        }

        enrollmentRepo.save(oldEnrollment);
        studentsService.calculateGpa(oldEnrollment.getStudent());

    }

    @Override
    @Transactional
    public void addEnrollment(Enrollment enrollment) throws RuntimeException{

        if (enrollmentRepo.existsById(enrollment.getId())) {
            throw new EnrollmentAlreadyExistsException("Enrollment with id " + enrollment.getId() + " already exists");
        }

        if (enrollment.getStudent() == null) {
            throw new EnrollmentValidationException("Student not found");
        }

        if (enrollment.getCourse() == null) {
             throw new EnrollmentValidationException("Course not found");
        }

        if (enrollment.getCourse().getMaxCapacity() <= enrollment.getCourse().getCurrentEnrollment())
        {
            throw new EnrollmentValidationException("Can't add enrollment, the course has reached the max capacity");
        }

        if (!enrollment.getCourse().getIsActive()){
            throw new EnrollmentValidationException("Can't add enrollment, the course is inactive");
        }

        if(!enrollment.getId().getSemester().isActive()){
            throw new EnrollmentValidationException("Can't add enrollment, the enrollment has been inactive for this semester.");
        }



        if (enrollment.getCourse().getPrerequisiteCourses() != null && !enrollment.getCourse().getPrerequisiteCourses().isEmpty()){

        for (Course course : enrollment.getCourse().getPrerequisiteCourses()) {
            if(enrollment.getStudent().getCompletedCourses() != null
                    && !enrollment.getStudent().getCompletedCourses().isEmpty()){

            boolean hasCompleted = enrollment.getStudent().getCompletedCourses().stream().
                    anyMatch(courses -> courses.getId().equals(course.getId()));


            if (!hasCompleted){

        if(enrollment.getStudent().getCompletedCourses().isEmpty() && !enrollment.getCourse().getPrerequisiteCourses().isEmpty())
                throw new RuntimeException("Can't add enrollment, You have to complete prerequisite courses!");
        }

                }                throw new EnrollmentValidationException("Can't add enrollment, You have to complete prerequisite courses!");
            }
        }



        //save enrollment
            enrollment.getCourse().setCurrentEnrollment(enrollment.getCourse().getCurrentEnrollment()+1);
            if(enrollment.getStatus()== EnrollmentStatus.COMPLETED){
                enrollment.getStudent().getCompletedCourses().add(enrollment.getCourse());
            }
            //save course, student, and enrollment

//            studentsService.calculateGpa(enrollment.getStudent());
            coursesRepository.save(enrollment.getCourse());
            enrollmentRepo.save(enrollment);
            studentsRepository.save(enrollment.getStudent());
        studentsService.calculateGpa(enrollment.getStudent());

    }

    @Override
    @Transactional
    public void deleteEnrollmentById(EnrollmentId id) {
        Enrollment enrollment = enrollmentRepo.findById(id).orElseThrow( () -> new RuntimeException("Enrollment not found"));
        enrollment.getCourse().setCurrentEnrollment(Math.max(0,enrollment.getCourse().getCurrentEnrollment()-1));
        coursesRepository.save(enrollment.getCourse());

        enrollmentRepo.deleteById(id);
    }


    public List<Enrollment> getEnrollmentsByStudentAndStatus(Long studentId, EnrollmentStatus status) {
        return enrollmentRepo.findEnrollmentsByStudentIdAndStatus(studentId, status);
    }

    @Override
    @Transactional
    public void dropEnrollment(EnrollmentId id) {
        Enrollment enrollment = enrollmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        if (enrollment.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new IllegalArgumentException("Can only drop active enrollments");
        }

        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepo.save(enrollment);

        Course course = enrollment.getCourse();
        if (course.getCurrentEnrollment() > 0) {
            course.setCurrentEnrollment(course.getCurrentEnrollment() - 1);
            coursesRepository.save(course);
        }
    }
}
