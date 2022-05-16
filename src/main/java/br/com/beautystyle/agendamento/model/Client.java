package br.com.beautystyle.agendamento.model;

import br.com.beautystyle.agendamento.repository.ClientRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;
    @NotNull
    private String name;
    private String phone;
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Event> eventList=new ArrayList<>();

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
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

    public Client update(ClientRepository clientRepository) {
        Client client = clientRepository.getById(this.clientId);
        client.setName(this.name);
        client.setPhone(this.phone);
        return client;
    }
}
