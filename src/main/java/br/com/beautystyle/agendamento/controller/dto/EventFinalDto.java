package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Event;
import br.com.beautystyle.agendamento.model.entity.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EventFinalDto {

    private EventDto event;
    private CustomerDto customer;
    private Set<JobDto> jobs;

    public EventFinalDto(Event event) {
        this.event = new EventDto(event);
        this.customer = new CustomerDto(event.getCostumer());
        this.jobs = JobDto.convertList(event.getJobs());
    }

    public EventFinalDto(Schedule schedule) {
        this.event = new EventDto(schedule);
        this.customer = new CustomerDto(schedule.getCustomer());
        this.jobs = JobDto.convert(schedule.getJobs());
    }

    public static List<EventFinalDto> convertList(List<Event> events) {
        List<EventFinalDto> eventsFinalDto = new ArrayList<>();
        events.forEach(event -> eventsFinalDto.add(new EventFinalDto(event)));
        return eventsFinalDto;
    }

    public EventFinalDto() {
    }

    public static List<EventFinalDto> convert(List<Schedule> schedules) {
        List<EventFinalDto> eventsFinalDto = new ArrayList<>();
        schedules.forEach(schedule -> eventsFinalDto.add(new EventFinalDto(schedule)));
        return eventsFinalDto;
    }

    public EventDto getEvent() {
        return event;
    }

    public void setEvent(EventDto event) {
        this.event = event;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public Set<JobDto> getJobs() {
        return jobs;
    }

    public void setJobs(Set<JobDto> jobs) {
        this.jobs = jobs;
    }

}
