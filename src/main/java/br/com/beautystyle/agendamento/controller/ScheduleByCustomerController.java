package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ScheduleByCostumerDto;
import br.com.beautystyle.agendamento.controller.dto.EventTimeDto;
import br.com.beautystyle.agendamento.controller.form.EventAvailableTimesForm;
import br.com.beautystyle.agendamento.controller.form.ScheduleByCostumerForm;
import br.com.beautystyle.agendamento.service.ScheduleByCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/event/by_customer")
public class ScheduleByCustomerController {

    @Autowired
    private ScheduleByCustomerService scheduleByCustomerService;

    @GetMapping
    public ResponseEntity<List<ScheduleByCostumerDto>> findAll() {
        return ResponseEntity.ok(scheduleByCustomerService.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ScheduleByCostumerDto> insert(@RequestBody @Valid ScheduleByCostumerForm eventForm) {
        return ResponseEntity.ok(scheduleByCustomerService.insert(eventForm));
    }

    @PostMapping("/available_time")
    public ResponseEntity<List<EventTimeDto>> availableTimes(@RequestBody @Valid EventAvailableTimesForm eventForm) {
        return ResponseEntity.ok(scheduleByCustomerService.getAvailableTimes(eventForm));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ScheduleByCostumerForm eventForm) {
        return ResponseEntity.ok(scheduleByCustomerService.update(id, eventForm));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        scheduleByCustomerService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
