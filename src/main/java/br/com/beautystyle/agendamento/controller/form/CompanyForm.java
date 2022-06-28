package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.entity.Address;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.model.entity.User;

public class CompanyForm {

    private String cnpj;
    private String name;
    private User user;
    private Address address;

    public CompanyForm() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public Company converter() {
        Company company = new Company();
        company.setAddress(this.address);
        company.setName(this.name);
        company.setUser(this.user);
        company.setCnpj(this.cnpj);
        return company;
    }
}
