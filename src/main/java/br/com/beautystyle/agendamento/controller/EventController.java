package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.EventDto;
import br.com.beautystyle.agendamento.model.Event;
import br.com.beautystyle.agendamento.model.Job;
import br.com.beautystyle.agendamento.repository.ClientRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @GetMapping("/{eventDate}")
    public ResponseEntity<List<EventDto>> getByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate eventDate) {
        List<Event> eventList = eventRepository.findByEventDate(eventDate);
        if (eventList != null) {
            return ResponseEntity.ok(EventDto.convertList(eventList));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{eventId}/jobList")
    public ResponseEntity<EventDto> enrollJobListToEvent(@RequestBody List<Job> jobList, @PathVariable Long eventId, UriComponentsBuilder uriBuilder) {
        Event event = eventRepository.getById(eventId);
        event.enrollJobList(jobList);
        eventRepository.save(event);
        URI uri = uriBuilder.path("/event/{id}")
                .buildAndExpand(event.getEventId())
                .toUri();
        return ResponseEntity.created(uri).body(new EventDto(event));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EventDto> insert(@RequestBody @Valid EventDto eventDto, UriComponentsBuilder uriBuilder) {
        Event event = eventDto.convert();
        Event newEvent = eventRepository.save(event);
        URI uri = uriBuilder.path("/event/{id}")
                .buildAndExpand(newEvent.getEventId())
                .toUri();
        return ResponseEntity.created(uri).body(new EventDto(newEvent));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<EventDto> update(@RequestBody @Valid EventDto eventDto) {
        Event event = eventDto.convertToUpdate();
        Optional<Event> eventOptional = eventRepository.findById(event.getEventId());
        if (eventOptional.isPresent()) {
            Event updatedEvent = event.update(eventRepository);
            return ResponseEntity.ok(new EventDto(updatedEvent));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            eventRepository.delete(eventOptional.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}
