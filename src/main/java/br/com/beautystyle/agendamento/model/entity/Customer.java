package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.CustomerForm;
import br.com.beautystyle.agendamento.repository.CustomerRepository;
import br.com.beautystyle.agendamento.repository.ScheduleRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(indexes = @Index(columnList = "tenant"))
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private String phone;
    @JsonIgnore
    @OneToMany(mappedBy = "customer",
            fetch = FetchType.LAZY)
    private List<Schedule> schedules = new ArrayList<>();
    @OneToOne(mappedBy = "customer")
    private UserCustomer userCustomer;
    @NotNull
    private Long tenant;

    public Customer() {
    }

    public Customer(Long id) {
        this.id = id;
    }

    public Customer(String name, String phone, Long tenant) {
        this.name = name;
        this.phone = phone;
        this.tenant = tenant;
    }

    public Customer(CustomerForm customerForm, Long tenant) {
        this.name = customerForm.getName();
        this.tenant = tenant;
        this.phone = customerForm.getPhone();
    }

    public Long getId() {
        if (id != null) return id;

        return -1L;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public UserCustomer getUserCustomer() {
        return userCustomer;
    }

    public void setUserCustomer(UserCustomer userCustomer) {
        this.userCustomer = userCustomer;
    }

    public boolean isUserCostumerNotNull() {
        return userCustomer != null;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public void removeAssociation(Schedule schedule) {
        this.schedules.remove(schedule);
    }

    public static List<Customer> convert(List<CustomerForm> customers, Long tenant) {
        return customers.stream().map(customer -> new Customer(customer,tenant)).collect(Collectors.toList());
    }

    public void update(CustomerForm customerForm) {
        this.name = customerForm.getName();
        this.phone = customerForm.getPhone();
    }

}
