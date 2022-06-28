package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.dto.JobDto;
import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.JobRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal valueOfJob;
    @NotNull
    private LocalTime durationTime;
    @ManyToMany(mappedBy = "jobList")
    @JsonIgnore
    private List<Event> eventList = new ArrayList<>();
    @NotNull
    private Long companyId;

    public static Set<Job> convert(Set<JobDto> jobDtoList) {
        return jobDtoList.stream().map(Job::new).collect(Collectors.toSet());
    }

    public Job (JobDto jobDto){
        this.jobId = jobDto.getApiId();
        this.name = jobDto.getName();
        this.companyId = jobDto.getCompanyId();
        this.valueOfJob = jobDto.getValueOfJob();
        this.durationTime = jobDto.getDurationTime();
    }

    public Job() {
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

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

    public void removeAssociation(JobRepository jobRepository, EventRepository eventRepository) {
        Job jobById = jobRepository.getById(this.jobId);
        jobById.getEventList().forEach(event -> {
            Event eventById = eventRepository.getById(event.getEventId());
            eventById.getJobList().remove(jobById);
        });
        jobById.getEventList().clear();
    }

    public void removeEvent(JobRepository jobRepository, Event event) {
        Job jobById = jobRepository.getById(this.jobId);
        jobById.getEventList().remove(event);
    }
}
