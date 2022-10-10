package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.BlockTimeDto;
import br.com.beautystyle.agendamento.controller.dto.EventByProfessionalDto;
import br.com.beautystyle.agendamento.controller.dto.EventFinalDto;
import br.com.beautystyle.agendamento.controller.exceptions.TenantNotEqualsException;
import br.com.beautystyle.agendamento.controller.form.EventByProfessionalForm;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.repository.*;
import br.com.beautystyle.agendamento.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;

@RestController
@RequestMapping("/event/by_professional")
public class EventByProfessionalController {

    @Autowired
    private JobTestRepository jobRepository;

    @Autowired
    private TokenServices tokenServices;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BlockTimeOnDayRepository blockTimeOnDayRepository;

    @Autowired
    private BlockTimeEveryDayRepository blockTimeEveryDayRepository;

    @Autowired
    private BlockWeekDayTimeRepository blockWeekDayTimeRepository;

    @Autowired
    private CustomerTestRepository customerRepository;

    @Autowired
    private EventTimeComponent eventTimeComponent;

    @Autowired
    private TenantComponent tenantComponent;

    @GetMapping("/{eventDate}")
    public ResponseEntity<EventByProfessionalDto> getScheduleByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate eventDate,
            HttpServletRequest request) {

        Long tenant = tokenServices.getTenant(request);

        List<Schedule> schedules = scheduleRepository.findByDateAndTenant(eventDate, tenant);

        EventTimeUtil eventTimeUtil = new EventTimeUtil(blockTimeOnDayRepository,
                blockTimeEveryDayRepository,
                blockWeekDayTimeRepository);

        BlockTimeDto blockTimes = eventTimeUtil.getBlockTimes(tenant, eventDate);
        List<EventFinalDto> events = EventFinalDto.convert(schedules);
        EventByProfessionalDto eventsDto = new EventByProfessionalDto(blockTimes, events);

        return ResponseEntity.ok(eventsDto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> insert(@RequestBody @Valid EventByProfessionalForm eventForm,
                                    HttpServletRequest request,
                                    UriComponentsBuilder uriBuilder) {

        Long tenant = tokenServices.getTenant(request);
        Schedule schedule = new Schedule(eventForm, tenant);

//        if (schedule.isInvalidIdOrTenantNotEquals(eventForm.getIds(), eventTimeRepository))
//            throw new EntityNotFoundException(ENTITY_NOT_FOUND_OR_TENANT_NOT_EQUALS);

        EventTimeUtil eventTimeUtil = new EventTimeUtil(eventTimeComponent);
        List<EventTime> eventTimes = eventTimeUtil.getEventTimes(tenant, schedule.getDate());

        EventTimeValidation eventTimeValidation = new EventTimeValidation(eventTimes);
        eventTimeValidation.validate(schedule);

        Schedule savedSchedule = scheduleRepository.save(schedule);
        URI uri = uriBuilder.path("/event/{id}")
                .buildAndExpand(savedSchedule.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new EventFinalDto(savedSchedule));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody @Valid EventByProfessionalForm eventForm,
                                    HttpServletRequest request) {

        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {

            Long tenant = tokenServices.getTenant(request);
            Schedule schedule = new Schedule(eventForm, tenant);

            TenantUtil tenantUtil = new TenantUtil(tenantComponent);
            tenantUtil.getOptionalTenants(eventForm.getCustomerId(),eventForm.getJobsTest());
            TenantValidation tenantValidation = new TenantValidation(jobRepository,customerRepository);
            tenantValidation.validate(eventForm);
//            if (optionalSchedule.get().isTenantNotEquals(tenant)
//                    || schedule.isInvalidIdOrTenantNotEquals(eventForm.getIds(), eventTimeRepository))
//                throw new EntityNotFoundException(ENTITY_NOT_FOUND_OR_TENANT_NOT_EQUALS);

            EventTimeUtil eventTimeUtil = new EventTimeUtil(eventTimeComponent);
            List<EventTime> eventTimes = eventTimeUtil.getEventTimes(tenant, schedule.getDate());
            EventTimeValidation eventTimeValidation = new EventTimeValidation(eventTimes);
            eventTimeValidation.validate(schedule);

            schedule.updateByProfessional(scheduleRepository, id);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException(SCHEDULE_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpServletRequest request) {

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        if (scheduleOptional.isPresent()) {
            Long tenant = tokenServices.getTenant(request);
            Schedule schedule = scheduleOptional.get();

            if (schedule.isTenantNotEquals(tenant))
                throw new TenantNotEqualsException(TENANT_NOT_EQUALS);

            schedule.removeAssociation(scheduleRepository, jobRepository);
            scheduleRepository.deleteById(id);

            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException(SCHEDULE_NOT_FOUND);
    }

}
