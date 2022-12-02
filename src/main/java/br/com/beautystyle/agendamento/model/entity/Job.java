package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.JobForm;
import br.com.beautystyle.agendamento.repository.JobRepository;
import br.com.beautystyle.agendamento.repository.ScheduleRepository;
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
@Table(indexes = @Index(columnList = "tenant"))
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
    private List<Schedule> schedules = new ArrayList<>();
    @NotNull
    private Long tenant;

    public Job() {
    }

    public Job(JobForm jobForm) {
        this.name = jobForm.getName();
        this.price = jobForm.getPrice();
        this.durationTime = jobForm.getDurationTime();
        this.tenant = jobForm.getTenant();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalTime getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(LocalTime durationTime) {
        this.durationTime = durationTime;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public LocalTime sumJobsDurationTime(LocalTime jobsDurationTime) {
        return jobsDurationTime.plusHours(this.durationTime.getHour())
                .plusMinutes(this.durationTime.getMinute());
    }

    public void removeAssociation(Schedule schedule) {
        this.schedules.remove(schedule);
    }

}
