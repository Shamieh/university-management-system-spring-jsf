package com.ats.project.service;

import com.ats.project.model.Nationality;


import java.util.List;

public interface NationalityService {
    Nationality createNationality(Nationality nationality);
    List<Nationality> findAll();
    Nationality findById(long id);
    void deleteById(long id);
    void updateNationality(Nationality nationality);
}
