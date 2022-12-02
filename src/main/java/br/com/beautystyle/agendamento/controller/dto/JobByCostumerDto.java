package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Job;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

public class JobByCostumerDto {

    private Long apiId;
    private String name;
    private LocalTime durationTime;
    private BigDecimal price;

    public static Set<JobByCostumerDto> convert(Set<Job> jobs) {
        return jobs.stream().map(JobByCostumerDto::new).collect(Collectors.toSet());
    }

    public JobByCostumerDto() {
    }

    public JobByCostumerDto(Job job) {
        this.name = job.getName();
        this.durationTime = job.getDurationTime();
        this.price = job.getPrice();
        this.apiId = job.getId();
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(LocalTime durationTime) {
        this.durationTime = durationTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
