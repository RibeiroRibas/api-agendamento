package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.JobEventForm;
import br.com.beautystyle.agendamento.controller.form.JobForm;
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
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private LocalTime durationTime;
    @ManyToMany(mappedBy = "jobs")
    @JsonIgnore
    private List<Event> eventList = new ArrayList<>();
    @NotNull
    private Long tenant;

    public Job() {}

    public Job(JobEventForm jobEventForm) {
        this.id = jobEventForm.getApiId();
    }

    public Job(JobForm jobForm) {
        this.name = jobForm.getName();
        this.price = jobForm.getPrice();
        this.durationTime = jobForm.getDurationTime();
        this.tenant = jobForm.getTenant();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public void setEventList(Event event) {
        this.eventList.add(event);
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void removeAssociation(JobRepository jobRepository, EventRepository eventRepository) {
        Job jobById = jobRepository.getById(this.id);
        jobById.getEventList().forEach(event -> {
            Event eventById = eventRepository.getById(event.getId());
            eventById.getJobs().remove(jobById);
        });
        jobById.getEventList().clear();
    }

    public LocalTime sumJobsDurationTime(LocalTime jobsDurationTime) {
        return jobsDurationTime.plusHours(this.durationTime.getHour())
                .plusMinutes(this.durationTime.getMinute());
    }

    public boolean isTenantNotEquals(Long tenant) {
        return !this.tenant.equals(tenant);
    }
}
