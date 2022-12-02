package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.entity.Profiles;
import br.com.beautystyle.agendamento.model.entity.UserProfessional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UserProfessionalForm {

    @NotNull
    @Valid
    private UserForm userForm;
    @NotNull
    @Valid
    private CompanyForm companyForm;

    public UserProfessionalForm() {
    }

    public UserProfessionalForm(UserForm userForm, CompanyForm companyForm) {
        this.userForm = userForm;
        this.companyForm = companyForm;
    }

    public UserForm getUserForm() {
        return userForm;
    }

    public void setUserForm(UserForm userForm) {
        this.userForm = userForm;
    }

    public CompanyForm getCompanyForm() {
        return companyForm;
    }

    public void setCompanyForm(CompanyForm companyForm) {
        this.companyForm = companyForm;
    }

    public UserProfessional converter(Profiles profile) {
        UserProfessional user = userForm.convertProfessional(profile);
        user.setCompany(companyForm.converter());
        return user;
    }
}
