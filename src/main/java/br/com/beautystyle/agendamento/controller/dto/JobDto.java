package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Job;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

public class JobDto {

    private Long apiId;
    private String name;
    private BigDecimal price;
    private LocalTime durationTime;
    private Long tenant;

    public JobDto(Job job) {
        this.apiId = job.getId();
        this.name = job.getName();
        this.price = job.getPrice();
        this.durationTime = job.getDurationTime();
        this.tenant = job.getTenant();
    }

    public static Set<JobDto> convertList(Set<Job> jobList) {
        return jobList.stream().map(JobDto::new).collect(Collectors.toSet());
    }

    public JobDto() {
    }

    public static Set<JobDto> convert(Set<Job> jobs) {
        return jobs.stream().map(JobDto::new).collect(Collectors.toSet());
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public LocalTime getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(LocalTime durationTime) {
        this.durationTime = durationTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

}
