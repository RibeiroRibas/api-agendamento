package br.com.beautystyle.agendamento.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleByProfessionalForm {

    @NotNull
    private Long customerId;
    @NotNull
    @NotEmpty
    private List<Long> jobIds;
    @NotNull
    private LocalDate eventDate;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private BigDecimal value;
    @NotNull
    private boolean hasPaymentReceived;

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public boolean isHasPaymentReceived() {
        return hasPaymentReceived;
    }

    public void setHasPaymentReceived(boolean hasPaymentReceived) {
        this.hasPaymentReceived = hasPaymentReceived;
    }

    public ScheduleByProfessionalForm() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Long> getJobIds() {
        return jobIds;
    }

    public void setJobIds(List<Long> jobIds) {
        this.jobIds = jobIds;
    }

}
