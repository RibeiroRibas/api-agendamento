package br.com.beautystyle.agendamento.controller.exceptions;

public class EntityNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
