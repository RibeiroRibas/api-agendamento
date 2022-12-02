package br.com.beautystyle.agendamento.controller.exceptions;

import javax.persistence.PersistenceException;

public class EventTimeNotAvailableException extends PersistenceException {

    public EventTimeNotAvailableException(String message) {
        super(message);
    }
}
