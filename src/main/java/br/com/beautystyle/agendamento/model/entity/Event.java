package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.dto.EventDto;
import br.com.beautystyle.agendamento.controller.dto.EventFinalDto;
import br.com.beautystyle.agendamento.controller.dto.JobDto;
import br.com.beautystyle.agendamento.model.StatusPagamento;
import br.com.beautystyle.agendamento.repository.ClientRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.JobRepository;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    @ManyToOne
    @JoinColumn(name = "client_id")
    @NotNull
    private Client client;
    @ManyToMany
    @JoinTable(name = "event_jobs",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "job_id")})
    @NotNull
    private Set<Job> jobList = new HashSet<>();
    @NotNull
    private LocalDate eventDate;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private BigDecimal valueEvent;
    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusPagamento statusPagamento;
    @NotNull
    private Long companyId;


    public Event(EventFinalDto eventFinalDto) {
        EventDto event = eventFinalDto.getEvent();
        this.eventId = event.getApiId();
        this.companyId = event.getCompanyId();
        this.eventDate = event.getEventDate();
        this.endTime = event.getEndTime();
        this.startTime = event.getStarTime();
        this.statusPagamento = event.getStatusPagamento();
        this.valueEvent = event.getValueEvent();
        this.client = new Client(eventFinalDto.getClient());
        this.jobList = Job.convert(eventFinalDto.getJobs());
    }

    public Event() {
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Set<Job> getJobList() {
        return jobList;
    }

    public void setJobList(Set<Job> jobList) {
        this.jobList = jobList;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BigDecimal getValueEvent() {
        return valueEvent;
    }

    public void setValueEvent(BigDecimal valueEvent) {
        this.valueEvent = valueEvent;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void enrollJobs(Set<Job> jobList) {
        this.jobList.clear();
        this.jobList.addAll(jobList);
    }

    public EventDto convert() {
        EventDto eventDto = new EventDto();
        eventDto.setApiId(this.eventId);
        eventDto.setCompanyId(this.companyId);
        eventDto.setEventDate(this.eventDate);
        eventDto.setEndTime(this.endTime);
        eventDto.setStarTime(this.startTime);
        eventDto.setStatusPagamento(this.statusPagamento);
        eventDto.setValueEvent(this.valueEvent);
        return eventDto;
    }

    public void removeAssociation(EventRepository eventRepository, JobRepository jobRepository, ClientRepository clientRepository) {
        Event eventById = eventRepository.getById(this.eventId);
        eventById.getJobList().forEach(job -> {
            Job jobById = jobRepository.getById(job.getJobId());
            jobById.getEventList().remove(eventById);
        });
        eventById.getJobList().clear();

//        Client clientById = clientRepository.getById(this.client.getClientId());
//        clientById.getEventList().remove(eventById);
//        eventById.setClient(new Client());
    }
}
