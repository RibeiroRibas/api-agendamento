package br.com.beautystyle.agendamento.model;

import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.JobRepository;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    @ManyToOne
    private Client client;
    @ManyToMany
    @JoinTable(name = "event_job",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "job_id")})
    private List<Job> jobList = new ArrayList<>();
    private LocalDate eventDate;
    private LocalTime starTime;
    private LocalTime endTime;
    private BigDecimal valueEvent;
    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<Job> getJobList() {
        return jobList;
    }

    public void setJobList(List<Job> jobList) {
        this.jobList = jobList;
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

    public LocalTime getStarTime() {
        return starTime;
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

    public void setStarTime(LocalTime starTime) {
        this.starTime = starTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public Event update(EventRepository eventRepository) {
        Event event = eventRepository.getById(this.eventId);
        update(event);
        return event;
    }

    private void update(Event event) {
        event.setClient(this.client);
        event.setEventDate(this.eventDate);
        event.setValueEvent(this.valueEvent);
        event.setStarTime(this.starTime);
        event.setEndTime(this.endTime);
        event.setStatusPagamento(this.statusPagamento);
        event.getJobList().clear();
        event.enrollJobList(jobList);
    }

    public void enrollJobList(List<Job> jobList) {
        this.jobList.addAll(jobList);
    }


}
