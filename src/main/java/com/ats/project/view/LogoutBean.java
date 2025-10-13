package com.ats.project.view;

import java.io.Serializable;

import jakarta.inject.Named;
import org.springframework.stereotype.Component;
import jakarta.faces.context.FacesContext;

@Named("logoutBean")
@jakarta.faces.view.ViewScoped
@Component
public class LogoutBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
}