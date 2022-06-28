package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ExpenseDto;
import br.com.beautystyle.agendamento.controller.dto.ReportDto;
import br.com.beautystyle.agendamento.model.entity.Expense;
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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/report/{id}/{startDate}/{endDate}")
    public List<ReportDto> getReportByPerid(@PathVariable Long id,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate startDate,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate endDate) {
        List<Expense> expenseList =
                expenseRepository.findByCompanyIdEqualsAndExpenseDateGreaterThanEqualAndExpenseDateLessThanEqual(
                        id, startDate, endDate);
        return ReportDto.convertExpenseList(expenseList);
    }

    @GetMapping("/report/{id}/{date}")
    public List<ReportDto> getReportByDate(@PathVariable Long id,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate date) {
        List<Expense> expenseList = expenseRepository.findByCompanyIdAndExpenseDate(id,date);
        return ReportDto.convertExpenseList(expenseList);
    }

    @GetMapping("/{companyId}")
    @Cacheable(value = "years")
    public List<String> getByYearsList(@PathVariable Long companyId) {
        List<LocalDate> expenses = expenseRepository.getYearsList(companyId);
        return expenses.stream()
                .map(LocalDate::getYear)
                .distinct()
                .sorted(Comparator.comparing(Integer::intValue))
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = {"expenseList", "years"}, allEntries = true)
    public ResponseEntity<ExpenseDto> insert(@RequestBody @Valid ExpenseDto expenseDto, UriComponentsBuilder uriBuilder) {
        Expense savedExpense = expenseRepository.save(expenseDto.convert());
        URI uri = uriBuilder.path("/expense/{id}")
                .buildAndExpand(savedExpense.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new ExpenseDto(savedExpense));
    }

    @PutMapping
    @Transactional
    @CacheEvict(value = {"expenseList", "years"}, allEntries = true)
    public ResponseEntity<ExpenseDto> update(@RequestBody @Valid ExpenseDto expenseDto) {
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseDto.getApiId());
        if (expenseOptional.isPresent()) {
            Expense expenseupdated = expenseDto.update(expenseRepository);
            return ResponseEntity.ok(new ExpenseDto(expenseupdated));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = {"expenseList", "years"}, allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isPresent()) {
            expenseRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
