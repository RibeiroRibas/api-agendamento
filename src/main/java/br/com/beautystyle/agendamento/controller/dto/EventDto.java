package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.Client;
import br.com.beautystyle.agendamento.model.Event;
import br.com.beautystyle.agendamento.model.Job;
import br.com.beautystyle.agendamento.model.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EventDto {

    private Long eventId;
    private Client client;
    private LocalDate eventDate;
    private LocalTime starTime;
    private LocalTime endTime;
    private BigDecimal valueEvent;
    private StatusPagamento statusPagamento;
    private List<Job> jobList;

    public EventDto(Event event) {
        this.eventId = event.getEventId();
        this.client = event.getClient();
        this.eventDate = event.getEventDate();
        this.starTime = event.getStarTime();
        this.endTime = event.getEndTime();
        this.valueEvent = event.getValueEvent();
        this.statusPagamento = event.getStatusPagamento();
        this.jobList = event.getJobList();
    }

    public EventDto() {
    }

    public static List<EventDto> convertList(List<Event> eventList) {
        return eventList.stream().map(EventDto::new).collect(Collectors.toList());
    }

    public Event convert() {
        Event event = new Event();
        event.setEventDate(this.eventDate);
        event.setValueEvent(this.valueEvent);
        event.setStatusPagamento(this.statusPagamento);
        event.setStarTime(this.starTime);
        event.setEndTime(this.endTime);
        event.setClient(this.client);
        event.setJobList(this.jobList);
        return event;
    }

    public Event convertToUpdate() {
        Event event = convert();
        event.setEventId(this.eventId);
        return event;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
    }

    public boolean isTimeAvaliable(List<Event> eventList) {
        return isStartTimeAvaliable(eventList) && isEndTimeAvaliable(eventList);
    }

    public boolean isEndTimeAvaliable(List<Event> eventList) {
        for (Event ev : eventList) {
            if (ev.getStarTime().isAfter(getStarTime())
                    && getEndTime().isAfter(ev.getStarTime())) {
                this.eventId = 0L;
                this.endTime = ev.getStarTime();
                return false;
            }
        }
        return true;
    }

    public boolean isStartTimeAvaliable(List<Event> eventList) {
        if (checkStartTime(eventList)) {return true;}
        this.eventId = 0L;
        this.endTime = null;
        return false;
    }

    private boolean checkStartTime(List<Event> eventList) {
        return eventList.stream()
                .filter(ev -> !ev.getEventId().equals(getEventId()))
                .anyMatch(event1 -> !getStarTime().isBefore(event1.getStarTime()) &&
                        getStarTime().isBefore(event1.getEndTime()));
    }
}
