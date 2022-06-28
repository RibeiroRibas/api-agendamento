package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Job;
import br.com.beautystyle.agendamento.repository.JobRepository;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JobDto {

    private Long apiId;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal valueOfJob;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY, pattern = "HH:mm:ss")
    private LocalTime durationTime;
    private Long companyId;

    public static Set<JobDto> convert(Set<Job> jobList) {
        return jobList.stream().map(JobDto::new).collect(Collectors.toSet());
    }

    public JobDto() {
    }

    public JobDto(Job job) {
        this.apiId = job.getJobId();
        this.companyId = job.getCompanyId();
        this.name = job.getName();
        this.valueOfJob = job.getValueOfJob();
        this.durationTime = job.getDurationTime();
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public void setValueOfJob(BigDecimal valueOfJob) {
        this.valueOfJob = valueOfJob;
    }

    public BigDecimal getValueOfJob() {
        return valueOfJob;
    }

    public Job convert() {
        Job job = new Job();
        job.setName(this.name);
        job.setDurationTime(this.durationTime);
        job.setValueOfJob(this.valueOfJob);
        job.setCompanyId(this.companyId);
        return job;
    }

    public void update(JobRepository jobRepository) {
        Job job = jobRepository.getById(this.apiId);
        job.setName(this.name);
        job.setValueOfJob(this.valueOfJob);
        job.setDurationTime(this.durationTime);
    }

}
