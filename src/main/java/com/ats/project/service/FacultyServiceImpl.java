package com.ats.project.service;

import com.ats.project.repository.FacultyRepository;
import com.ats.project.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.findByName(faculty.getName())
                .orElseGet(() -> facultyRepository.save(faculty));
    }

    @Override
    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public Optional<Faculty> findById(Long id) {
        return facultyRepository.findById(id);
    }

    @Override
    public void deleteFaculty(Long id) {
        if (facultyRepository.findById(id).isPresent()) {
                facultyRepository.delete(findById(id).get());
 }else
            throw new RuntimeException("Faculty with id " + id + " not found");
    }

    @Override
    public void updateFaculty(Faculty updatedFaculty) {
         Faculty oldFaculty = facultyRepository.findById(updatedFaculty.getId()).get();
         oldFaculty.setName(updatedFaculty.getName());

         facultyRepository.save(oldFaculty);

    }

}

