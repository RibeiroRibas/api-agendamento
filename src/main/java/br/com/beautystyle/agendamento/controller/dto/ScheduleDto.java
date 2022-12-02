package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Schedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleDto {

    private Long apiId;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal value;
    private Long tenant;
    private boolean hasPaymentReceived;

    public ScheduleDto() {
    }

    public ScheduleDto(Schedule schedule) {
        this.apiId = schedule.getId();
        this.eventDate = schedule.getDate();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.value = schedule.getPrice();
        this.tenant = schedule.getTenant();
        this.hasPaymentReceived = schedule.isHasPaymentReceived();
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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setHasPaymentReceived(boolean hasPaymentReceived) {
        this.hasPaymentReceived = hasPaymentReceived;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public boolean isHasPaymentReceived() {
        return hasPaymentReceived;
    }
}
