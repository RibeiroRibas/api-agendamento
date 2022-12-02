package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Address;

public class AddressDto {

    private String street;
    private int number;
    private String zipCode;
    private String city;
    private String state;
    private String country;

    public AddressDto() {
    }

    public AddressDto(Address address) {
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.zipCode = address.getZipCode();
        this.city = address.getCity();
        this.state = address.getState();
        this.country = address.getCountry();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
