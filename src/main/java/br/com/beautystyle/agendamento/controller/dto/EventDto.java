package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Client;
import br.com.beautystyle.agendamento.model.entity.Event;
import br.com.beautystyle.agendamento.model.entity.Job;
import br.com.beautystyle.agendamento.model.StatusPagamento;
import br.com.beautystyle.agendamento.repository.EventRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventDto {

    private Long apiId;
    private LocalDate eventDate;
    private LocalTime starTime;
    private LocalTime endTime;
    private BigDecimal valueEvent;
    private StatusPagamento statusPagamento;
    private Long companyId;

    public EventDto() {
    }

    public EventDto(Event event) {
        this.apiId = event.getEventId();
        this.eventDate = event.getEventDate();
        this.starTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.valueEvent = event.getValueEvent();
        this.statusPagamento = event.getStatusPagamento();
        this.companyId = event.getCompanyId();
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getStarTime() {
        return starTime;
    }

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getValueEvent() {
        return valueEvent;
    }

    public void setValueEvent(BigDecimal valueEvent) {
        this.valueEvent = valueEvent;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public boolean isTimeAvaliable(List<Event> eventList) {
        return isStartTimeAvaliable(eventList) && isEndTimeAvaliable(eventList);
    }

    public boolean isEndTimeAvaliable(List<Event> eventList) {
        for (Event ev : eventList) {
            if (ev.getStartTime().isAfter(getStarTime())
                    && getEndTime().isAfter(ev.getStartTime())) {
                this.apiId = 0L;
                this.endTime = ev.getStartTime();
                return false;
            }
        }
        return true;
    }

    public boolean isStartTimeAvaliable(List<Event> eventList) {
        if (checkStartTime(eventList)) {return true;}
        this.apiId = 0L;
        this.endTime = null;
        return false;
    }

    private boolean checkStartTime(List<Event> eventList) {
        return eventList.stream()
                .filter(ev -> !ev.getEventId().equals(getApiId()))
                .anyMatch(event1 -> !getStarTime().isBefore(event1.getStartTime()) &&
                        getStarTime().isBefore(event1.getEndTime()));
    }



}
