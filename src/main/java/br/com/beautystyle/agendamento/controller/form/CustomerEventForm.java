package br.com.beautystyle.agendamento.controller.form;

import javax.validation.constraints.NotNull;

public class CustomerEventForm {

    @NotNull
    private Long apiId;

    public CustomerEventForm() {
    }

    public CustomerEventForm(Long apiId) {
        this.apiId = apiId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
}
