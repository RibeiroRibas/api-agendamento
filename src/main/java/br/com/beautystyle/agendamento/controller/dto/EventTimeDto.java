package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.EventTime;

import java.time.LocalTime;

public class EventTimeDto {

    private LocalTime startTime;
    private LocalTime endTime;

    public EventTimeDto(EventTime eventTime) {
        this.startTime = eventTime.getStartTime();
        this.endTime = eventTime.getEndTime();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
