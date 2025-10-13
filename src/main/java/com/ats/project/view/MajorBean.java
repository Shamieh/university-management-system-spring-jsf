package com.ats.project.view;

import com.ats.project.model.Major;
import com.ats.project.service.MajorService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("majorBean")
@Getter
@Setter
@NoArgsConstructor


public class MajorBean implements Serializable {

    @Autowired
    private MajorService majorService;

    private String name;
    private Long id;
    private Major selectedMajor;

    List<Major> majorList;

    public MajorBean(MajorService majorService) {
        this.majorService = majorService;
    }


    @PostConstruct
    public void init() {

        majorList = new ArrayList<>();
        refershMajors();
    }


    public void resetForm() {
        selectedMajor = new Major();
    }



    public void deleteMajor(Major major) {
        try {
            majorService.deleteMajor(major.getId());
            refershMajors();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Success", "Major deleted successfully!"));
        } catch (DataIntegrityViolationException ex) {
            // Spring Data JPA throws this when a foreign key fails
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Cannot delete major: there are students linked to this major."));
        } catch (RuntimeException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", e.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Unexpected error occurred while deleting major."));
        }
    }


    private void refershMajors() {
        majorList = majorService.findAll();
    }

    public void saveMajor(){
        try {
            if (selectedMajor.getId() == null) {
                // Adding a new major
                majorService.saveMajor(selectedMajor);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Success", "Major added successfully!"));
            } else {
                // Updating an existing major
                majorService.updateMajor(selectedMajor);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Success", "Major updated successfully!"));
            }
        } catch (RuntimeException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Operation failed: " + e.getMessage()));
            return; // Stop execution to avoid resetting the form if failed
        }

        // Refresh and reset only if operation succeeded
        refershMajors();
        resetForm();
    }

}
