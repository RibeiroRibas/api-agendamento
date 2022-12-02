package br.com.beautystyle.agendamento.controller.exceptions;


import javax.persistence.*;

public class EventDateNotAvailableException extends PersistenceException {
    public EventDateNotAvailableException(String message) {
        super(message);
    }
}
