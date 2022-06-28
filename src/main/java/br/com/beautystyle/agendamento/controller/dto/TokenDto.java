package br.com.beautystyle.agendamento.controller.dto;

import java.util.List;

public class TokenDto {

    private String token;
    private String type;
    private Long companyId;
    private List<String> profiles;

    public TokenDto(String token, String type, Long companyId, List<String> profiles) {
        this.token = token;
        this.type = type;
        this.companyId = companyId;
        this.profiles = profiles;
    }

    public TokenDto(String token, String bearer) {
        this.token = token;
        this.type = bearer;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public List<String> getProfiles() {
        return profiles;
    }
}
