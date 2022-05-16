package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.Client;
import br.com.beautystyle.agendamento.repository.ClientRepository;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class ClientForm {

    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Client converter() {
        Client client = new Client();
        client.setName(this.name);
        client.setPhone(this.phone);
        return client;
    }
}
