package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ScheduleByProfessionalDto;
import br.com.beautystyle.agendamento.controller.form.ScheduleByProfessionalForm;
import br.com.beautystyle.agendamento.service.ScheduleByProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/event/by_professional")
public class ScheduleByProfessionalController {

    @Autowired
    private ScheduleByProfessionalService scheduleService;


    @GetMapping("/{eventDate}")
    public ResponseEntity<ScheduleByProfessionalDto> getScheduleByDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate eventDate) {
        return ResponseEntity.ok(scheduleService.findAllByDate(eventDate));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> insert(@RequestBody @Valid ScheduleByProfessionalForm eventForm) {
        return scheduleService.insert(eventForm);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ScheduleByProfessionalForm eventForm) {
        return scheduleService.update(eventForm, id);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
            scheduleService.deleteById(id);
            return ResponseEntity.ok().build();
    }

}
