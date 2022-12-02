package br.com.beautystyle.agendamento.controller.form;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class EventAvailableTimesForm {

    @NotNull
    private LocalDate eventDate;
    @NotNull
    private Set<Long> jobIds;

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Set<Long> getJobIds() {
        return jobIds;
    }

    public void setJobIds(Set<Long> jobIds) {
        this.jobIds = jobIds;
    }
}
