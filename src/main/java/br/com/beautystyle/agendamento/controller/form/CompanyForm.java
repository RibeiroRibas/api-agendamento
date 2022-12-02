package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.CompanyType;
import br.com.beautystyle.agendamento.model.entity.Address;
import br.com.beautystyle.agendamento.model.entity.BusinessHours;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


public class CompanyForm {

    @NotNull
    private String rn;
    @NotNull
    private String name;
    @NotNull
    @Valid
    private Address address;
    @NotNull
    @Valid
    private List<OpeningHoursForm> openingHours;
    @NotNull
    private String description;
    @Enumerated(EnumType.STRING)
    @NotNull
    private CompanyType companyType;

    public CompanyForm() {
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OpeningHoursForm> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHoursForm> openingHours) {
        this.openingHours = openingHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String businessHours) {
        this.description = businessHours;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public Company converter() {
        Company company = new Company();
        company.setAddress(this.address);
        company.setName(this.name);
        company.setRn(this.rn);
        company.setBusinessHours(new BusinessHours(this.openingHours));
        company.setType(this.companyType);
        return company;
    }

    public void update(CompanyRepository companyRepository, Long tenant) {
        Company company = companyRepository.getById(tenant);
        company.setRn(rn);
        company.setName(name);
        company.setAddress(address);
        company.setBusinessHours(new BusinessHours(this.openingHours));
        company.setType(this.companyType);
    }

}
