package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Client;
import br.com.beautystyle.agendamento.model.entity.Event;
import br.com.beautystyle.agendamento.model.entity.Job;
import br.com.beautystyle.agendamento.repository.ClientRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.JobRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventFinalDto {

    private EventDto event;
    private ClientDto client;
    private Set<JobDto> jobs;

    public EventFinalDto(Event event) {
        this.event = new EventDto(event);
        this.client = new ClientDto(event.getClient());
        this.jobs = JobDto.convert(event.getJobList());
    }

    public static List<EventFinalDto> convertList(List<Event> events) {
        return events.stream().map(EventFinalDto::new).collect(Collectors.toList());
    }

    public EventFinalDto() {
    }

    public EventDto getEvent() {
        return event;
    }

    public void setEvent(EventDto event) {
        this.event = event;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public Set<JobDto> getJobs() {
        return jobs;
    }

    public void setJobs(Set<JobDto> jobs) {
        this.jobs = jobs;
    }

    public void update(EventRepository eventRepository, JobRepository jobRepository) {
        EventDto eventDto = this.event;
        Event event = eventRepository.getById(eventDto.getApiId());
        event.setEventDate(eventDto.getEventDate());
        event.setValueEvent(eventDto.getValueEvent());
        event.setStartTime(eventDto.getStarTime());
        event.setEndTime(eventDto.getEndTime());
        event.setStatusPagamento(eventDto.getStatusPagamento());
        event.setClient(new Client(this.client));
        event.getJobList().forEach(job-> job.removeEvent(jobRepository,event));
        event.setJobList(Job.convert(this.jobs));
    }

}
