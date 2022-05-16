package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.form.ExpenseForm;
import br.com.beautystyle.agendamento.model.Expense;
import br.com.beautystyle.agendamento.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping
    @Cacheable(value = "expenseList")
    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "expenseList", allEntries = true)
    public ResponseEntity<Expense> insert(@RequestBody @Valid ExpenseForm expense, UriComponentsBuilder uriBuilder) {
        Expense savedExpense = expenseRepository.save(expense.convert());
        URI uri = uriBuilder.path("/expense/{id}")
                .buildAndExpand(savedExpense.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedExpense);
    }

    @PutMapping
    @Transactional
    @CacheEvict(value = "expenseList", allEntries = true)
    public ResponseEntity<Expense> update(@RequestBody @Valid Expense expense){
        Optional<Expense> expenseOptional = expenseRepository.findById(expense.getId());
        if(expenseOptional.isPresent()){
            Expense expenseUpdated = expense.update(expenseRepository);
           return ResponseEntity.ok(expenseUpdated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "expenseList", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Expense> expense = expenseRepository.findById(id);
        if(expense.isPresent()){
            expenseRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
