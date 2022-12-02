package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EventDto {

    private ScheduleDto event;
    private CustomerDto customer;
    private Set<JobDto> jobs;


    public EventDto(Schedule schedule) {
        this.event = new ScheduleDto(schedule);
        this.customer = new CustomerDto(schedule.getCustomer());
        this.jobs = JobDto.convert(schedule.getJobs());
    }

    public EventDto() {
    }

    public static List<EventDto> convert(List<Schedule> schedules) {
        List<EventDto> eventsFinalDto = new ArrayList<>();
        schedules.forEach(schedule -> eventsFinalDto.add(new EventDto(schedule)));
        return eventsFinalDto;
    }

    public ScheduleDto getEvent() {
        return event;
    }

    public void setEvent(ScheduleDto event) {
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
