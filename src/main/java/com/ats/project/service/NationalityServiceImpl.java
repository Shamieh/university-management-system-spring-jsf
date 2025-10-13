package com.ats.project.service;

import com.ats.project.repository.NationalityRepository;
import com.ats.project.model.Nationality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationalityServiceImpl implements NationalityService {
    private final NationalityRepository nationalityRepository;

    @Autowired
    public NationalityServiceImpl(NationalityRepository nationalityRepository) {
        this.nationalityRepository = nationalityRepository;
    }

    @Override
    public List<Nationality> findAll() {
        return nationalityRepository.findAll();
    }

    @Override
    public Nationality findById(long id) {
        return nationalityRepository.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        nationalityRepository.deleteById(id);
    }

    @Override
    public void updateNationality(Nationality nationality) {
        Nationality n = findById(nationality.getId());
        n.setName(nationality.getName());
        nationalityRepository.save(n);
    }


    @Override
    public Nationality createNationality(Nationality nationality) {
        if (nationalityRepository.findByName(nationality.getName()).isPresent()) {
            throw new IllegalArgumentException("Nationality with name " + nationality.getName() + " already exists");
        } else
           return nationalityRepository.save(nationality);
    }
}




