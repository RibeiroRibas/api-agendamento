package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Event;
import br.com.beautystyle.agendamento.model.entity.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportDto {

    private String clientName;
    private LocalDate date;
    private BigDecimal eventValue;
    private String expenseCategory;
    private BigDecimal expenseValue;

    public ReportDto() {
    }

    public ReportDto(Expense expense) {
        this.date = expense.getExpenseDate();
        this.expenseValue = expense.getPrice();
        this.expenseCategory = expense.getCategory();
    }

    public ReportDto(Event event) {
        this.date = event.getEventDate();
        this.eventValue = event.getValueEvent();
        this.clientName = event.getClient().getName();
    }

    public static List<ReportDto> convertExpenseList(List<Expense> expenseList) {
        return expenseList.stream().map(ReportDto::new).collect(Collectors.toList());
    }

    public static List<ReportDto> convertEventList(List<Event> eventList) {
        return eventList.stream().map(ReportDto::new).collect(Collectors.toList());
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getEventValue() {
        return eventValue;
    }

    public void setEventValue(BigDecimal eventValue) {
        this.eventValue = eventValue;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public BigDecimal getExpenseValue() {
        return expenseValue;
    }

    public void setExpenseValue(BigDecimal expenseValue) {
        this.expenseValue = expenseValue;
    }
}
