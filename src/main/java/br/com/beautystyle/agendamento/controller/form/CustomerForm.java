package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.controller.dto.CustomerDto;
import br.com.beautystyle.agendamento.model.entity.Customer;
import br.com.beautystyle.agendamento.repository.CustomerRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CustomerForm {

    @NotNull
    private String name;
    private String phone;
    @JsonIgnore
    private Long tenant;

    public static List<CustomerDto> convert(List<Customer> customerList) {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        for (Customer customer : customerList) {
            customerDtoList.add(new CustomerDto(customer));
        }
        return customerDtoList;
    }

    public CustomerForm(Customer customer) {
        this.name = customer.getName();
        this.phone = customer.getPhone();
    }

    public CustomerForm() {
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
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

    public Customer converter() {
        Customer customer = new Customer();
        customer.setName(this.name);
        customer.setPhone(this.phone);
        customer.setTenant(this.tenant);
        return customer;
    }


    public Customer update(Long id, CustomerRepository repository) {
        Customer customer = repository.getById(id);
        customer.setName(this.name);
        customer.setPhone(this.phone);
        return customer;
    }
}
