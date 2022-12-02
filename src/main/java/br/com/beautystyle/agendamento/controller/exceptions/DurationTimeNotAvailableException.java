package br.com.beautystyle.agendamento.controller.exceptions;


import javax.persistence.*;

public class DurationTimeNotAvailableException extends PersistenceException {

    public DurationTimeNotAvailableException(String message) {
        super(message);
    }



}
