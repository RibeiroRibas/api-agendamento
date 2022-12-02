package br.com.beautystyle.agendamento.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@MappedSuperclass
public abstract class BlockTime extends EventTime {

    @NotNull
    private LocalDate date;
    private String reason;

    public BlockTime() {
    }

    public BlockTime(LocalTime startTime, LocalTime endTime) {
        super(startTime, endTime);
    }

    public BlockTime(Long id, LocalTime startTime, LocalTime endTime, Long tenant) {
        super(id, startTime, endTime, tenant);
    }

    public BlockTime(Long id, LocalTime startTime, LocalTime endTime) {
        super(id, startTime, endTime);
    }

    public BlockTime(LocalTime startTime, LocalTime endTime, String reason) {
        super(startTime, endTime);
    }

    public BlockTime(LocalTime startTime, LocalTime endTime, LocalDate date, String reason, Long tenant) {
        super(startTime, endTime);
        setTenant(tenant);
        this.date = date;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
