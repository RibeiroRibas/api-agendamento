package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Company;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyDto {

    private String name;
    private String companyType;
    private Long tenant;
    private AddressDto address;
    private String businessHour;

    public CompanyDto() {
    }

    public CompanyDto(Company company) {
        this.name = company.getName();
        this.tenant = company.getId();
        this.address = new AddressDto(company.getAddress());
        this.businessHour = company.getBusinessHours().getDescription();
        this.companyType = company.getType().getType();
    }

    public static List<CompanyDto> convert(List<Company> companyList) {
        return companyList.stream().map(CompanyDto::new).collect(Collectors.toList());
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getBusinessHour() {
        return businessHour;
    }

    public void setBusinessHour(String businessHour) {
        this.businessHour = businessHour;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
}
