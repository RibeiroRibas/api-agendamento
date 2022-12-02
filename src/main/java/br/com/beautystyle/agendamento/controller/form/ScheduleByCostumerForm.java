package br.com.beautystyle.agendamento.controller.form;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public class ScheduleByCostumerForm {

    @NotNull
    private LocalDate eventDate;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private Set<Long> jobIds;

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

    public Set<Long> getJobIds() {
        return jobIds;
    }

    public void setJobIds(Set<Long> jobIds) {
        this.jobIds = jobIds;
    }
}
