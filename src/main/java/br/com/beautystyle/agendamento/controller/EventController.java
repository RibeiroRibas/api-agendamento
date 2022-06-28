package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ClientDto;
import br.com.beautystyle.agendamento.controller.dto.EventDto;
import br.com.beautystyle.agendamento.controller.dto.EventFinalDto;
import br.com.beautystyle.agendamento.controller.dto.ReportDto;
import br.com.beautystyle.agendamento.model.entity.Client;
import br.com.beautystyle.agendamento.model.entity.Event;
import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.repository.ClientRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/{eventDate}/{companyId}")
    public ResponseEntity<List<EventFinalDto>> getByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate eventDate,
                                                         @PathVariable Long companyId) {
        List<Event> events = eventRepository.findByEventDateAndCompanyId(eventDate, companyId);
        if (events != null) {
            return ResponseEntity.ok(EventFinalDto.convertList(events));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EventFinalDto> insert(@RequestBody @Valid EventFinalDto eventFinalDto, UriComponentsBuilder uriBuilder) {
        Event newEvent = eventRepository.save(new Event(eventFinalDto));
        //eventFinalDto.updateClient(clientRepository,newEvent);
        URI uri = uriBuilder.path("/event/{id}")
                .buildAndExpand(newEvent.getEventId())
                .toUri();
        return ResponseEntity.created(uri).body(new EventFinalDto(newEvent));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid EventFinalDto eventFinalDto) {
        Optional<Event> eventOptional = eventRepository.findById(eventFinalDto.getEvent().getApiId());
        if (eventOptional.isPresent()) {
            eventFinalDto.update(eventRepository, jobRepository);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            eventOptional.get().removeAssociation(eventRepository, jobRepository, clientRepository);
            eventRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{companyId}")
    @Cacheable(value = "years")
    public List<String> getByYearsList(@PathVariable Long companyId) {
        List<LocalDate> expenses = eventRepository.getYearsList(companyId);
        return expenses.stream()
                .map(LocalDate::getYear)
                .distinct()
                .sorted(Comparator.comparing(Integer::intValue))
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    @GetMapping("/report/{id}/{startDate}/{endDate}")
    public List<ReportDto> getReportByPeriod(@PathVariable Long id,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate startDate,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate endDate) {
        List<Event> eventList =
                eventRepository.findByCompanyIdEqualsAndEventDateGreaterThanEqualAndEventDateLessThanEqual(
                        id, startDate, endDate);
        return ReportDto.convertEventList(eventList);
    }

    @GetMapping("/report/{id}/{date}")
    public List<ReportDto> getReportByDate(@PathVariable Long id,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate date) {
        List<Event> eventList = eventRepository.findByEventDateAndCompanyId(date,id);
        return ReportDto.convertEventList(eventList);
    }
}
