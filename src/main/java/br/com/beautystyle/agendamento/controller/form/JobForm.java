package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.Job;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;

public class JobForm {

    @NotNull
    private String name;
    @NotNull
    private BigDecimal valueOfJob;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY, pattern = "HH:mm")
    private LocalTime durationTime;

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
        return job;
    }

}
