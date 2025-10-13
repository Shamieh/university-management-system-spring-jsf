package com.ats.project.view;


import com.ats.project.model.Nationality;
import com.ats.project.service.NationalityService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage; // EDITED: Added import
import jakarta.faces.context.FacesContext; // EDITED: Added import
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException; // EDITED: Added import

import java.util.List;

@ViewScoped
@Named("nationalityBean")
@Getter
@Setter
public class NationalityBean {

    @Autowired
    NationalityService nationalityService;

    private Long id;
    private String name;

    private Nationality selectedNationality;
    private List<Nationality> nationalityList;

    @PostConstruct
    public void init(){
        selectedNationality = new Nationality();
        refreshNationalities();
    }

    public void refreshNationalities(){
        nationalityList = nationalityService.findAll();
    }

    public void resetForm(){
        selectedNationality = new Nationality();
    }

    // EDITED: Updated saveNationality method to match facultyBean pattern exactly
    public void saveNationality(){
        try {
            if (selectedNationality.getId() == null) {
                // Adding a new nationality
                nationalityService.createNationality(selectedNationality);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Success", "Nationality added successfully!"));
            } else {
                // Updating an existing nationality
                nationalityService.updateNationality(selectedNationality);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Success", "Nationality updated successfully!"));
            }
        } catch (RuntimeException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Operation failed: " + e.getMessage()));
            return; // Stop execution to avoid resetting the form if failed
        }

        // Refresh and reset only if operation succeeded
        refreshNationalities();
        resetForm();
    }

    // EDITED: Updated deleteNationality method to match facultyBean pattern
    public void deleteNationality(Nationality nationality){
        try {
            nationalityService.deleteById(nationality.getId());
            refreshNationalities();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Success", "Nationality deleted successfully!"));
        } catch (DataIntegrityViolationException ex) {
            // Spring Data JPA throws this when a foreign key fails
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Cannot delete nationality: there are students linked to this nationality."));
        } catch (RuntimeException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", e.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Unexpected error occurred while deleting nationality."));
        }
    }
}