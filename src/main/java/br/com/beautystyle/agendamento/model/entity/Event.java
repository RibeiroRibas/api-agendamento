package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.EventByCostumerForm;
import br.com.beautystyle.agendamento.controller.form.EventByProfessionalForm;
import br.com.beautystyle.agendamento.controller.form.EventForm;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.JobRepository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(indexes = @Index(columnList = "tenant"))
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_jobs",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "job_id")})
    @NotNull
    private Set<Job> jobs = new HashSet<>();
    @NotNull
    private LocalDate eventDate;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private BigDecimal value;
    @NotNull
    private Long tenant;
    @NotNull
    private boolean hasPaymentReceived;

    public Event() {
    }

    public Event(EventByProfessionalForm eventFinalForm, Long tenant) {
        EventForm event = eventFinalForm.getEvent();
        this.tenant = event.getTenant();
        this.eventDate = event.getEventDate();
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.value = event.getValue();
        this.hasPaymentReceived = event.isHasPaymentReceived();
        this.customer = eventFinalForm.getCustomer();
        this.jobs = eventFinalForm.getFoundJobs();
        this.tenant = tenant;
    }

    public Event(EventByCostumerForm event) {
        this.tenant = event.getTenant();
        this.eventDate = event.getEventDate();
        this.startTime = event.getStarTime();
        this.hasPaymentReceived = false;
        this.jobs = event.getFoundJobs();
        this.value = sumJobsValue();
        this.endTime = sumJobsDurationTime();
    }

    public long getTenant() {
        return tenant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public void setTenant(Long companyId) {
        this.tenant = companyId;
    }

    public Customer getCostumer() {
        return customer;
    }

    public void setCostumer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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

    public boolean isHasPaymentReceived() {
        return hasPaymentReceived;
    }

    public void setHasPaymentReceived(boolean hasPaymentReceived) {
        this.hasPaymentReceived = hasPaymentReceived;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void removeAssociation(EventRepository eventRepository, JobRepository jobRepository) {
        Event eventById = eventRepository.getById(this.id);
        eventById.getJobs().forEach(job -> {
            Job jobById = jobRepository.getById(job.getId());
            jobById.getEventList().remove(eventById);
        });
        eventById.getJobs().clear();
    }

    private BigDecimal sumJobsValue() {
        return jobs.stream().map(Job::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LocalTime sumJobsDurationTime() {
        LocalTime durationEvent = LocalTime.of(0, 0);
        for (Job job : jobs) {
            durationEvent = durationEvent.plusHours(job.getDurationTime().getHour())
                    .plusMinutes(job.getDurationTime().getMinute());
        }
        return startTime.plusHours(durationEvent.getHour()).plusMinutes(durationEvent.getMinute());
    }

    public LocalTime isDurationTimeNotAvailableThenReturnReducedDurationTime(List<Event> events,
                                                                             @NotNull Long id,
                                                                             List<BlockTime> blockTimes) {
        LocalTime reducedDurationTime = null;

        for (Event event : events) {
            if (isDurationTimeNotAvailable(event, id)) {
                reducedDurationTime = event.getStartTime();
                break;
            }
        }
        if (reducedDurationTime != null)
            return reducedDurationTime;

        for (BlockTime blockTime : blockTimes) {
            if (isDurationTimeNotAvailable(blockTimes)) {
                reducedDurationTime = blockTime.getStartTime();
                break;
            }
        }

        return reducedDurationTime;
    }

    public boolean isDurationTimeNotAvailable(List<Event> events,
                                              @NotNull Long id,
                                              List<BlockTime> blockTimes) {
        return events.stream()
                .anyMatch(event -> isDurationTimeNotAvailable(event, id)
                        || isDurationTimeNotAvailable(blockTimes));
    }

    private boolean isDurationTimeNotAvailable(List<BlockTime> blockTimes) {
        return blockTimes.stream()
                .anyMatch(blockTime -> blockTime.getStartTime().isAfter(startTime)
                        && endTime.isAfter(blockTime.getStartTime()));
    }

    private boolean isDurationTimeNotAvailable(Event event2, Long id2) {
        return event2.getStartTime().isAfter(startTime)
                && endTime.isAfter(event2.getStartTime())
                && !id2.equals(event2.getId());
    }

    public boolean isEventStartTimeNotAvailable(List<Event> events,
                                                Long id,
                                                List<BlockTime> blockTimes) {
        return events.stream()
                .anyMatch(ev -> !startTime.isBefore(ev.getStartTime())
                        && startTime.isBefore(ev.getEndTime())
                        && !ev.getId().equals(id)
                        || blockTimes.stream()
                        .anyMatch(blockTime -> !startTime.isBefore(blockTime.getStartTime())
                                && startTime.isBefore(blockTime.getEndTime())));
    }


    public boolean isStartTimeNotAvailable(LocalTime startTime) {
        return !startTime.isBefore(this.startTime) &&
                startTime.isBefore(endTime);
    }

    public boolean isEndTimeNotAvailable(LocalTime startTime, LocalTime endTime) {
        return this.startTime.isAfter(startTime)
                && endTime.isAfter(this.startTime);
    }

    public boolean isTenantNotEquals(Long tenant) {
        return !this.tenant.equals(tenant);
    }

    public void updateByProfessional(EventRepository eventRepository, Long id) {
        Event eventToUpdate = eventRepository.getById(id);
        eventToUpdate.setCostumer(customer);
        update(eventToUpdate);
    }

    public void update(Event eventToUpdate) {
        eventToUpdate.setEventDate(eventDate);
        eventToUpdate.setStartTime(startTime);
        eventToUpdate.setJobs(jobs);
        eventToUpdate.setHasPaymentReceived(hasPaymentReceived);
        eventToUpdate.setEndTime(endTime);
        eventToUpdate.setValue(value);
    }

    public boolean isTenantEquals(long tenant) {
        return this.tenant.equals(tenant);
    }

    public void removeCostumerAssociation(EventRepository eventRepository) {
        Event event = eventRepository.getById(id);
        event.setCostumer(null);
    }

    public boolean isCostumerNull() {
        return customer == null;
    }

    public Company getCompany(CompanyRepository companyRepository) {
        Optional<Company> company = companyRepository.findById(tenant);
        return company.orElse(null);
    }

    public LocalTime getDurationTime() {
        return endTime.minusHours(startTime.getHour())
                .minusMinutes(startTime.getMinute());
    }

    public boolean isUserIdNotEquals(Long userId) {
        if (isCostumerNull()) return true;
        return customer.isUserIdNotNullOrEquals(userId);
    }

    public boolean isEventStartingIn12hrsOrLess() {
        LocalDateTime eventDateAndTime = LocalDateTime.of(eventDate, startTime);
        LocalDateTime now = LocalDateTime.now();
        Duration timeLeft = Duration.between(now, eventDateAndTime);
        return timeLeft.toMinutes() <= 720;
    }


}
