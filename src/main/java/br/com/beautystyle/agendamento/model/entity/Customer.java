package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.CustomerEventForm;
import br.com.beautystyle.agendamento.controller.form.CustomerForm;
import br.com.beautystyle.agendamento.repository.EventRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
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
    private List<Event> events = new ArrayList<>();
    @OneToOne(mappedBy = "customer")
    private UserCustomer userCustomer;
    private Long tenant;

    public Customer() {

    }

    public Customer(CustomerForm customerForm) {
        this.name = customerForm.getName();
        this.tenant = customerForm.getTenant();
        this.phone = customerForm.getPhone();
    }

    public Customer(CustomerEventForm costumer) {
        this.id = costumer.getApiId();
    }

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public static List<Customer> convert(List<CustomerForm> clients) {
        return clients.stream().map(Customer::new).collect(Collectors.toList());
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEventList(Event event) {
        this.events.add(event);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void removeEventAssociation(EventRepository eventRepository) {
        events.forEach(event -> {
            Event eventToUpdate = eventRepository.getById(event.getId());
            eventToUpdate.setCostumer(null);
        });
        this.events.clear();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public UserCustomer getUserCustomer() {
        return userCustomer;
    }

    public void setUserCustomer(UserCustomer userCustomer) {
        this.userCustomer = userCustomer;
    }

    public void addEventAssociation(Event newEvent) {
        this.events.add(newEvent);
    }

    public boolean isUserIdNotNullOrEquals(Long userId) {
        return userCustomer == null || userCustomer.isUserIdNotEquals(userId);
    }

    public boolean isTenantNotEquals(Long tenant) {
        return !this.tenant.equals(tenant);
    }

    public boolean isUserCostumerNotNull() {
        return userCustomer != null;
    }
}
