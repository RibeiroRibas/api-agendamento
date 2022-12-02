package br.com.beautystyle.agendamento.controller.exceptions;

public class DurationTimeNotAllowException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DurationTimeNotAllowException(String message) {
        super(message);
    }
}
