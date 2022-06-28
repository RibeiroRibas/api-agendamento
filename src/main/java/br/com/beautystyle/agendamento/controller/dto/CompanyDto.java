package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Address;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.repository.CompanyRepository;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {

    private Long apiId;
    private String cnpj;
    private String name;
    private Address address;

    public CompanyDto() {
    }

    public CompanyDto(Company savedCompany) {
        this.apiId = savedCompany.getId();
        this.cnpj = savedCompany.getCnpj();
        this.name = savedCompany.getName();
        this.address = savedCompany.getAddress();
    }

    public static List<CompanyDto> convert(List<Company> companyList) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Company company : companyList) {
            companyDtoList.add(new CompanyDto(company));
        }
        return companyDtoList;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
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

    public Company update(CompanyRepository companyRepository) {
        Company company = companyRepository.getById(this.apiId);
        company.setCnpj(this.cnpj);
        company.setName(this.name);
        company.setAddress(this.address);
        return company;
    }
}
