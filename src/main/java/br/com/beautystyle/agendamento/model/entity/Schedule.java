package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.ScheduleByCostumerForm;
import br.com.beautystyle.agendamento.controller.form.ScheduleByProfessionalForm;
import br.com.beautystyle.agendamento.util.JobUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(indexes = @Index(columnList = "tenant"))
public class Schedule extends EventTime {

    @ManyToOne
    private Customer customer;
    @ManyToMany(fetch = FetchType.EAGER)
    @NotNull
    @JoinTable(name = "event_jobs",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "job_id")})
    private Set<Job> jobs = new HashSet<>();
    @NotNull
    private boolean hasPaymentReceived;
    @NotNull
    private BigDecimal price;
    @NotNull
    private LocalDate date;

    public Schedule() {
    }

    public  Schedule(ScheduleByProfessionalForm eventForm, Set<Job> jobs, Long tenant) {
        this.jobs = jobs;
        this.price = eventForm.getValue();
        this.hasPaymentReceived = eventForm.isHasPaymentReceived();
        this.date = eventForm.getEventDate();
        this.customer = new Customer(eventForm.getCustomerId());
        setStartTime(eventForm.getStartTime());
        LocalTime durationTime = JobUtil.sumJobsDurationTime(jobs);
        setEndTime(getEndTime(durationTime));
        setTenant(tenant);
    }

    private LocalTime getEndTime(LocalTime durationTime) {
        return  getStartTime()
                .plusHours(durationTime.getHour())
                .plusMinutes(durationTime.getMinute());
    }

    public Schedule(Long id, LocalTime startTime, LocalTime endTime) {
        super(id, startTime, endTime);
    }

    public Schedule(ScheduleByCostumerForm eventForm, Set<Job> jobs, Customer customer) {
        this.jobs = jobs;
        this.price = sumJobsValue();
        this.hasPaymentReceived = false;
        this.date = eventForm.getEventDate();
        this.customer = customer;
        setStartTime(eventForm.getStartTime());
        LocalTime durationTime = JobUtil.sumJobsDurationTime(jobs);
        setEndTime(getEndTime(durationTime));
        setTenant(getJobTenant());
    }

    public Schedule(LocalTime startTime, LocalTime endTime) {
        super(startTime, endTime);
    }

    public Schedule(ScheduleByCostumerForm eventForm, Set<Job> jobs, Long id) {
        this.jobs = jobs;
        this.price = sumJobsValue();
        this.date = eventForm.getEventDate();
        this.hasPaymentReceived = false;
        setId(id);
        setStartTime(eventForm.getStartTime());
        LocalTime durationTime = JobUtil.sumJobsDurationTime(jobs);
        setEndTime(getEndTime(durationTime));
        setTenant(getJobTenant());
    }

    private Long getJobTenant() {
        return jobs.stream().findFirst().get().getTenant();
    }

    public Schedule(LocalDate eventDate) {
        this.date = eventDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Customer getCustomer() {
        if (customer != null)
            return customer;
        return new Customer();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public boolean isHasPaymentReceived() {
        return hasPaymentReceived;
    }

    public void setHasPaymentReceived(boolean hasPaymentReceived) {
        this.hasPaymentReceived = hasPaymentReceived;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void updateByProfessional(Schedule schedule) {
        schedule.setCustomer(customer);
        update(schedule);
    }

    public void update(Schedule schedule) {
        this.date = schedule.getDate();
        this.setStartTime(schedule.getStartTime());
        this.jobs = schedule.getJobs();
        this.hasPaymentReceived = schedule.hasPaymentReceived;
        this.setEndTime(schedule.getEndTime());
        this.price = schedule.getPrice();
    }

    private BigDecimal sumJobsValue() {
        return jobs.stream().map(Job::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEventStartingIn12hrsOrLess() {
        LocalDateTime eventDateAndTime = LocalDateTime.of(date, getStartTime());
        LocalDateTime now = LocalDateTime.now();
        Duration timeLeft = Duration.between(now, eventDateAndTime);
        return timeLeft.toMinutes() <= 720;
    }

    public boolean isEventDateNotAvailable(List<OpeningHours> openingHours) {
        return openingHours.stream()
                .map(OpeningHours::getDayOfWeek)
                .distinct()
                .noneMatch(dayOfWeek -> dayOfWeek == date.getDayOfWeek().getValue());
    }

}
