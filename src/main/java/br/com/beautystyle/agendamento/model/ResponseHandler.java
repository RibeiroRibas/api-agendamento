package br.com.beautystyle.agendamento.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status code", status.value());
        return new ResponseEntity<>(map,status);
    }

    public static ResponseEntity<Object> generateResponseWithData(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("statusCode", status.value());
        map.put("data", responseObj);
        return new ResponseEntity<>(map,status);
    }
}
