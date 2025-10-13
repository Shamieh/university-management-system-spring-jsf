package com.ats.project.view;

import com.ats.project.model.Faculty;
import com.ats.project.service.FacultyService;
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

@Named("facultyBean")
@Getter
@Setter
@NoArgsConstructor


public class FacultyBean implements Serializable {

    @Autowired
    private FacultyService facultyService;

    private String name;
    private Long id;
    private Faculty selectedFaculty;

    List<Faculty> facultyList;

    public FacultyBean(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostConstruct
    public void init() {

        facultyList = new ArrayList<>();
        refreshFaculties();
    }


    public void resetForm() {
        selectedFaculty = new Faculty();
    }



    public void deleteFaculty(Faculty faculty) {
        try {
            facultyService.deleteFaculty(faculty.getId());
            refreshFaculties();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Success", "Faculty deleted successfully!"));
        } catch (DataIntegrityViolationException ex) {
            // Spring Data JPA throws this when a foreign key fails
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Cannot delete faculty: there are students linked to this faculty."));
        } catch (RuntimeException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", e.getMessage()));
        }catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Unexpected error occurred while deleting faculty."));
        }
    }



    private void refreshFaculties() {
        facultyList = facultyService.findAll();
    }

    public void saveFaculty(){
        try {
            if (selectedFaculty.getId() == null) {
                // Adding a new faculty
                facultyService.createFaculty(selectedFaculty);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Success", "Faculty added successfully!"));
            } else {
                // Updating an existing faculty
                facultyService.updateFaculty(selectedFaculty);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Success", "Faculty updated successfully!"));
            }
        } catch (RuntimeException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Operation failed: " + e.getMessage()));
            return; // Stop execution to avoid resetting the form if failed
        }

        // Refresh and reset only if operation succeeded
        refreshFaculties();
        resetForm();
    }


}
