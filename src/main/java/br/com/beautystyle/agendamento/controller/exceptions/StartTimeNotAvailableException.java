package br.com.beautystyle.agendamento.controller.exceptions;

import javax.persistence.*;

public class StartTimeNotAvailableException extends PersistenceException {

    public StartTimeNotAvailableException(String message) {
        super(message);
    }

}
