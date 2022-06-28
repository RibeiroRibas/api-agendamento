package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Client;
import br.com.beautystyle.agendamento.repository.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDto {

    private Long apiId;
    private String name;
    private String phone;
    private Long userId;
    private Long companyId;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public static List<ClientDto> convert(List<Client> clientList) {
        return clientList.stream().map(ClientDto::new).collect(Collectors.toList());
    }

    public ClientDto(Client client) {
        if(client!=null){
            this.apiId = client.getClientId();
            this.name = client.getName();
            this.phone = client.getPhone();
            this.userId = client.getUserId();
            this.companyId = client.getCompanyId();
        }
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

    public Client convert() {
        Client client = new Client();
        client.setName(this.name);
        client.setPhone(this.phone);
        client.setUserId(this.userId);
        client.setClientId(this.apiId);
        client.setCompanyId(this.companyId);
        return client;
    }


    public ClientDto() {
    }

    public Client update(ClientRepository repository) {
        Client client = repository.getById(this.apiId);
        client.setName(this.name);
        client.setPhone(this.phone);
        return client;
    }
}
