package br.com.beautystyle.agendamento.controller.exceptions;

public class RequestNotAllowException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RequestNotAllowException(String message) {
        super(message);
    }
}
