package com.ats.project.service;

import com.ats.project.repository.CoursesRepository;
import com.ats.project.model.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoursesServiceImp implements CoursesService {
    private final CoursesRepository coursesRepository;

    @Override
    public void deleteById(Long id) {
        coursesRepository.deleteById(id);
    }

    public CoursesServiceImp(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    @Override
    @Transactional
    public Course CreateCourses(Course courses) {
        Optional<Course> existingCourse = coursesRepository.findByName(courses.getName());
        if (existingCourse.isPresent()) {
            return existingCourse.get();
        }

        // If the course has prerequisites, ensure they're managed entities
        if (courses.getPrerequisiteCourses() != null && !courses.getPrerequisiteCourses().isEmpty()) {
            List<Course> managedPrerequisites = new ArrayList<>();
            for (Course prereq : courses.getPrerequisiteCourses()) {
                if (prereq.getId() != null) {
                    // Fetch the managed entity from database
                    Course managedPrereq = coursesRepository.findById(prereq.getId())
                            .orElseThrow(() -> new RuntimeException("Prerequisite course not found: " + prereq.getId()));
                    managedPrerequisites.add(managedPrereq);
                } else {
                    // If no ID, save it first
                    managedPrerequisites.add(coursesRepository.save(prereq));
                }
            }
            courses.setPrerequisiteCourses(managedPrerequisites);
        }

        return coursesRepository.save(courses);
    }
    @Override
    public List<Course> findAll() {
        return coursesRepository.findAll();
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return coursesRepository.findById(courseId);
    }

    @Transactional
    @Override
    public void updateCourses(Course updatedCourses) {
        Course oldCourse = coursesRepository.findById(updatedCourses.getId()).get();
        oldCourse.setName(updatedCourses.getName());
        oldCourse.setMaxCapacity(updatedCourses.getMaxCapacity());
        oldCourse.setCurrentEnrollment(updatedCourses.getCurrentEnrollment());
        oldCourse.setDescription(updatedCourses.getDescription());
        oldCourse.setIsActive(updatedCourses.getIsActive());
        oldCourse.setFaculty(updatedCourses.getFaculty());
        oldCourse.setCreditHours(updatedCourses.getCreditHours());

        coursesRepository.save(oldCourse);
    }


}
