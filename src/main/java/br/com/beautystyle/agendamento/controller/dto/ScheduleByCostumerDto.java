package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.model.entity.Schedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScheduleByCostumerDto {

    private Long apiId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate eventDate;
    private BigDecimal value;
    private Set<JobByCostumerDto> jobs;
    private CompanyDto company;
    private boolean hasPaymentReceived;

    public ScheduleByCostumerDto() {
    }

    public ScheduleByCostumerDto(Schedule schedule, Company company) {
        this.apiId = schedule.getId();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.eventDate = schedule.getDate();
        this.value = schedule.getPrice();
        this.jobs = JobByCostumerDto.convert(schedule.getJobs());
        this.company = new CompanyDto(company);
        this.hasPaymentReceived = schedule.isHasPaymentReceived();
    }

    public static List<ScheduleByCostumerDto> convert(List<Schedule> schedules, Company company) {
        return schedules.stream().map(schedule -> new ScheduleByCostumerDto(schedule, company)).collect(Collectors.toList());
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
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

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Set<JobByCostumerDto> getJobs() {
        return jobs;
    }

    public void setJobs(Set<JobByCostumerDto> jobs) {
        this.jobs = jobs;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public boolean isHasPaymentReceived() {
        return hasPaymentReceived;
    }

    public void setHasPaymentReceived(boolean hasPaymentReceived) {
        this.hasPaymentReceived = hasPaymentReceived;
    }
}
