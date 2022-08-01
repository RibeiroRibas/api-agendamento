package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ReportDto;
import br.com.beautystyle.agendamento.model.entity.Event;
import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.repository.EventRepository;
import br.com.beautystyle.agendamento.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/{id}/{startDate}/{endDate}")
    public List<ReportDto> getReportByPeriod(@PathVariable Long id,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate startDate,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate endDate) {
        List<Expense> expenses =
                expenseRepository.findByCompanyIdEqualsAndExpenseDateGreaterThanEqualAndExpenseDateLessThanEqual(
                        id, startDate, endDate);
        List<ReportDto> report = ReportDto.convertExpenseList(expenses);
        List<Event> events =
                eventRepository.findByCompanyIdEqualsAndEventDateGreaterThanEqualAndEventDateLessThanEqual(
                        id, startDate, endDate);
        report.addAll(ReportDto.convertEventList(events));
        return report;
    }

    @GetMapping("/{id}/{date}")
    public List<ReportDto> getReportByDate(@PathVariable Long id,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate date) {
        List<Expense> expenses = expenseRepository.findByCompanyIdAndExpenseDate(id,date);
        List<ReportDto> report = ReportDto.convertExpenseList(expenses);
        List<Event> events = eventRepository.findByEventDateAndCompanyId(date, id);
        report.addAll(ReportDto.convertEventList(events));
        return report;
    }

    @GetMapping("/{companyId}")
    public List<String> getByYearsList(@PathVariable Long companyId) {
        List<LocalDate> expenseDates = expenseRepository.getYearsList(companyId);
        List<String> years = getYears(expenseDates);
        List<LocalDate> eventDates = eventRepository.getYearsList(companyId);
        years.addAll(getYears(eventDates));
        return years.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getYears(List<LocalDate> expenseDates) {
        return expenseDates.stream()
                .map(LocalDate::getYear)
                .sorted(Comparator.comparing(Integer::intValue))
                .map(Objects::toString)
                .collect(Collectors.toList());
    }
}
