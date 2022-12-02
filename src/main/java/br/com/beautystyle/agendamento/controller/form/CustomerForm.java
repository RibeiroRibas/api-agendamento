package br.com.beautystyle.agendamento.controller.form;

import javax.validation.constraints.NotNull;

public class CustomerForm {

    @NotNull
    private String name;
    private String phone;

    public CustomerForm() {
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

}
