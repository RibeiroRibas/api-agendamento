package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.model.TimeAvailable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@MappedSuperclass
public abstract class EventTime implements Comparable<EventTime>, TimeAvailable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private Long tenant;

    public EventTime() {
    }

    public EventTime(Long id, LocalTime startTime, LocalTime endTime, Long tenant) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tenant = tenant;
    }

    public EventTime(Long id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EventTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
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

    @Override
    public int compareTo(EventTime o) {
        if (startTime.equals(o.getStartTime())) return 0;
        if (startTime.isAfter(o.getStartTime())) return 1;
        return -1;
    }

    @Override
    public Optional<EventTime> isStartTimeNotAvailable(List<EventTime> eventTimes) {
        return eventTimes.stream()
                .filter(eventTime ->
                        !getStartTime().isBefore(eventTime.getStartTime())
                                && getStartTime().isBefore(eventTime.getEndTime())
                                && isIdEquals(eventTime.getId())
                ).findFirst();
    }

    @Override
    public Optional<EventTime> isDurationTimeNotAvailable(List<EventTime> eventTimes) {
        return eventTimes.stream()
                .filter(eventTime ->
                        eventTime.getStartTime().isAfter(startTime)
                                && endTime.isAfter(eventTime.getStartTime())
                                && isIdEquals(eventTime.getId())
                ).findFirst();
    }

    @Override
    public boolean isEventTimeNotAvailable(List<EventTime> eventTimes) {
        return eventTimes.stream()
                .anyMatch(eventTime ->
                        eventTime.getStartTime().isAfter(startTime)
                                && endTime.isAfter(eventTime.getStartTime())
                                && isIdEquals(eventTime.getId())
                                || !startTime.isBefore(eventTime.getStartTime())
                                && startTime.isBefore(eventTime.getEndTime())
                                && isIdEquals(eventTime.getId())
                );
    }

    private boolean isIdEquals(Long id) {
        if(this.id == null) return true;
        return !this.id.equals(id);
    }

}

