package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.BusinessHours;

import java.util.List;

public class TokenDto {

    private String token;
    private String type;
    private Long tenant;
    private List<String> profiles;
    private List<OpeningHoursDto> openingHours;

    public TokenDto() {
    }

    public TokenDto(String token, String type, Long tenant, List<String> profiles, BusinessHours businessHours) {
        this.token = token;
        this.type = type;
        this.tenant = tenant;
        this.profiles = profiles;
        this.openingHours = OpeningHoursDto.converter(businessHours.getOpeningHours());
    }

    public TokenDto(String token, String bearer) {
        this.token = token;
        this.type = bearer;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Long getTenant() {
        return tenant;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public List<OpeningHoursDto> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHoursDto> openingHours) {
        this.openingHours = openingHours;
    }
}
