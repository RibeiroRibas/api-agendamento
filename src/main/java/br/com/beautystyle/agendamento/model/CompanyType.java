package br.com.beautystyle.agendamento.model;

public enum CompanyType {
    BARBERSHOP("Barbearia"), SALON("Salão de Beleza");

    private final String type;

    CompanyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
