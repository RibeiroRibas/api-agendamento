package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.entity.Job;
import br.com.beautystyle.agendamento.repository.JobRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;

public class JobForm {

    @NotNull
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private LocalTime durationTime;
    @JsonIgnore
    private Long tenant;

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
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

    public Job update(Long id, JobRepository jobRepository) {
        Job job = jobRepository.getById(id);
        job.setName(this.name);
        job.setPrice(this.price);
        job.setDurationTime(this.durationTime);
        return job;
    }
}
