package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.form.BlockTimeOnDayForm;
import br.com.beautystyle.agendamento.service.BlockTimeOnDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/block_time_on_day")
public class BlockTimeOnDayController {

    @Autowired
    private BlockTimeOnDayService blockTimeOnDayService;

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody @Valid BlockTimeOnDayForm blockTimeForm) {
        return blockTimeOnDayService.insert(blockTimeForm);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid BlockTimeOnDayForm blockTimeForm) {
        return blockTimeOnDayService.update(blockTimeForm, id);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        blockTimeOnDayService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

