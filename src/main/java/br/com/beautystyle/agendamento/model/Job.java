package br.com.beautystyle.agendamento.model;

import br.com.beautystyle.agendamento.repository.JobRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String name;
    private BigDecimal valueOfJob;
    private LocalTime durationTime;
    @ManyToMany(mappedBy = "jobList",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Event> eventList = new ArrayList<>();

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValueOfJob() {
        return valueOfJob;
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

    public void setValueOfJob(BigDecimal valueOfJob) {
        this.valueOfJob = valueOfJob;
    }


    public Job update(JobRepository jobRepository) {
        Job job = jobRepository.getById(this.jobId);
        job.setName(this.name);
        job.setValueOfJob(this.valueOfJob);
        job.setDurationTime(this.durationTime);
        return job;
    }

}
