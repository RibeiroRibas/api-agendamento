package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.controller.dto.ClientDto;
import br.com.beautystyle.agendamento.model.entity.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientForm {

    private String name;
    private String phone;
    private Long userId;
    private Long companyId;

    public static List<ClientDto> convert(List<Client> clientList) {
        List<ClientDto> clientDtoList = new ArrayList<>();
        for (Client client : clientList) {
            clientDtoList.add(new ClientDto(client));
        }
        return clientDtoList;
    }

    public ClientForm(Client client) {
        this.name = client.getName();
        this.phone = client.getPhone();
        this.userId = client.getUserId();
    }

    public ClientForm() {
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
        client.setUserId(this.userId);
        client.setCompanyId(this.companyId);
        return client;
    }
}
