package com.ats.project.service;

import com.ats.project.model.Major;

import java.util.List;

public interface MajorService {
    Major saveMajor(Major major);
    List<Major> findAll();
    Major findById(long id);

    void deleteMajor(Long id);

    void updateMajor(Major major);
}
