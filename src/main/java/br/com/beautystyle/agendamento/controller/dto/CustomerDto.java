package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Customer;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerDto {

    private Long apiId;
    private String name;
    private String phone;
    private Long tenant;
    private boolean isUser;

    public CustomerDto() {
    }

    public CustomerDto(Customer customer) {
        if(customer != null){
            this.apiId = customer.getId();
            this.name = customer.getName();
            this.phone = customer.getPhone();
            this.isUser = customer.isUserCostumerNotNull();
            this.tenant = customer.getTenant();
        }
    }

    public static List<CustomerDto> convert(List<Customer> customerList) {
        return customerList.stream().map(CustomerDto::new).collect(Collectors.toList());
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

}
