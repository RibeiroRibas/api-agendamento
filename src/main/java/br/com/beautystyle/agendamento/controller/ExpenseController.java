package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.ExpenseDto;
import br.com.beautystyle.agendamento.controller.exceptions.TenantNotEqualsException;
import br.com.beautystyle.agendamento.controller.form.ExpenseForm;
import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.repository.ExpenseRepository;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private TokenServices tokenServices;

    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<List<ExpenseDto>> getByPeriod(@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate startDate,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate endDate,
                                                        HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        List<Expense> expenseList =
                expenseRepository.findByTenantEqualsAndDateGreaterThanEqualAndDateLessThanEqual(
                        tenant, startDate, endDate);
        return ResponseEntity.ok(ExpenseDto.convert(expenseList));
    }


    @GetMapping
    @Cacheable(value = "years")
    public List<String> getByYearsList(HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        List<LocalDate> expensesDate = expenseRepository.getYearsListByTenant(tenant);
        return expensesDate.stream()
                .map(LocalDate::getYear)
                .distinct()
                .sorted(Comparator.comparing(Integer::intValue))
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = {"expenseList", "years"}, allEntries = true)
    public ResponseEntity<?> insert(@RequestBody @Valid ExpenseForm expenseForm,
                                    UriComponentsBuilder uriBuilder,
                                    HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        expenseForm.setTenant(tenant);
        Expense savedExpense = expenseRepository.save(new Expense(expenseForm));
        URI uri = uriBuilder.path("/expense/{id}")
                .buildAndExpand(savedExpense.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new ExpenseDto(savedExpense));
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = {"expenseList", "years"}, allEntries = true)
    public ResponseEntity<ExpenseDto> update(@PathVariable Long id,
                                             @RequestBody @Valid ExpenseForm expenseForm,
                                             HttpServletRequest request) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            Long tenant = tokenServices.getTenant(request);
            if (optionalExpense.get().isTenantNotEquals(tenant))
                throw new TenantNotEqualsException(TENANT_NOT_EQUALS);
            Expense expenseUpdated = expenseForm.update(expenseRepository, id);
            return ResponseEntity.ok(new ExpenseDto(expenseUpdated));
        }
        throw new EntityNotFoundException(ENTITY_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = {"expenseList", "years"}, allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpServletRequest request) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);
        if (optionalExpense.isPresent()) {
            Long tenant = tokenServices.getTenant(request);
            if (optionalExpense.get().isTenantNotEquals(tenant))
                throw new TenantNotEqualsException(TENANT_NOT_EQUALS);
            expenseRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException(ENTITY_NOT_FOUND);
    }

}
