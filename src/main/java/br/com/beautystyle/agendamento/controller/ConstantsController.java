package br.com.beautystyle.agendamento.controller;

public interface ConstantsController {

    Long INVALID_ID = -1L;
    String COMPANY_NOT_FOUND = "A empresa solicitada nao foi encontrada.";
    String JOB_NOT_FOUND = " O Servico selecionado nao foi encontrado!";
    String CATEGORY_NOT_FOUND = "A Categoria selecionada nao foi encontrada!";
    String TENANT_NOT_EQUALS = "Os registros selecionados não pertencem da mesma empresa";
    String CUSTOMER_NOT_FOUND = "O Cliente selecionado nao foi encontrado!";
    String ENTITY_NOT_FOUND_OR_TENANT_NOT_EQUALS = "Um ou mais registros não foram encontrados ou não pertencem a mesma empresa!";
    String SCHEDULE_NOT_FOUND = "O Agendamento selecionado nao foi encontrado!";
    String ENTITY_NOT_FOUND = "Um ou mais registros não foram encontrados.";
    String BAD_CREDENTIALS = "Email ou senha inválidos.";
    String USER_PROFILE_P = "ROLE_PREMIUM_ACCOUNT";
    String USER_PROFILE_C = "ROLE_CUSTOMER";
    String DURATION_TIME_IS_NOT_AVAILABLE = "Tempo de duração excede o tempo disponível na agenda! ";
    String START_TIME_IS_NOT_AVAILABLE = "Horario Inicial Indisponivel ";
    String EVENT_TIME_IS_NOT_AVAILABLE = "Horário Indisponivel";
    String EVENT_DATE_NOT_AVAILABLE = "A data selecionada não está disponível para novos agendamentos";
    String UPDATE_OR_DELETE_NOT_ALLOW = "Não é possível atualizar ou deletar um agendamento 12 horas antes do horário marcado";
    String REQUEST_OK = "Requisição bem sucedida";
    String CAUSED_BY = "Causado por: ";
}
