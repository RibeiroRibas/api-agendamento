package br.com.beautystyle.agendamento.controller.exceptions;


import javax.persistence.*;

public class TenantNotEqualsException extends PersistenceException {

    public TenantNotEqualsException(String message) {super((message));}
}
