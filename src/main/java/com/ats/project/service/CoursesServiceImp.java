package com.ats.project.service;

import com.ats.project.model.Students;
import com.ats.project.repository.CoursesRepository;
import com.ats.project.model.Courses;
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
    public Courses CreateCourses(Courses courses) {
        Optional<Courses> existingCourse = coursesRepository.findByName(courses.getName());
        if (existingCourse.isPresent()) {
            return existingCourse.get();
        }

        // If the course has prerequisites, ensure they're managed entities
        if (courses.getPrerequisiteCourses() != null && !courses.getPrerequisiteCourses().isEmpty()) {
            List<Courses> managedPrerequisites = new ArrayList<>();
            for (Courses prereq : courses.getPrerequisiteCourses()) {
                if (prereq.getId() != null) {
                    // Fetch the managed entity from database
                    Courses managedPrereq = coursesRepository.findById(prereq.getId())
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
    public List<Courses> findAll() {
        return coursesRepository.findAll();
    }

    @Override
    public Optional<Courses> findById(Long courseId) {
        return coursesRepository.findById(courseId);
    }

    @Transactional
    @Override
    public void updateCourses(Courses updatedCourses) {
        Courses oldCourse = coursesRepository.findById(updatedCourses.getId()).get();
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
