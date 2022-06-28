package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.dto.ClientDto;
import br.com.beautystyle.agendamento.controller.form.ClientForm;
import br.com.beautystyle.agendamento.repository.ClientRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    @NotNull
    private String name;
    private String phone;
    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private Set<Event> events = new HashSet<>();
    private Long userId;
    @NotNull
    private Long companyId; // id Company

    public Client(ClientDto clientDto) {
        this.clientId = clientDto.getApiId();
        this.name = clientDto.getName();
        this.phone = clientDto.getPhone();
        this.userId = clientDto.getUserId();
        this.companyId = clientDto.getCompanyId();
    }

    public Client() {
    }

    public Client(ClientForm clientForm) {
        this.name = clientForm.getName();
        this.companyId = clientForm.getCompanyId();
        this.userId = clientForm.getUserId();
        this.phone = clientForm.getPhone();
    }

    public static List<Client> convert(List<ClientForm> clients) {
        return clients.stream().map(Client::new).collect(Collectors.toList());
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void setEventList(Event event) {
        this.events.add(event);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void removeAssociation(EventRepository eventRepository) {
        List<Event> events = eventRepository.findByClientClientId(this.clientId);
        events.forEach(event -> {
            Event eventById = eventRepository.getById(event.getEventId());
            eventById.setClient(null);
        });
    }

    public void addEvent(Event newEvent) {
        this.events.add(newEvent);
    }
}
