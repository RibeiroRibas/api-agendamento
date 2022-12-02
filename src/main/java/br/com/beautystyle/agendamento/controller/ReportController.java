package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.ReportDto;
import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {


    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TokenServices tokenServices;

    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<List<ReportDto>> getReportByPeriod(@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate startDate,
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate endDate,
                                                             HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        List<Expense> expenses =
                expenseRepository.findByTenantEqualsAndDateGreaterThanEqualAndDateLessThanEqual(
                        tenant, startDate, endDate);
        List<ReportDto> report = ReportDto.convertExpenseList(expenses);
//        List<Event> events =
//                eventRepository.findByTenantEqualsAndEventDateGreaterThanEqualAndEventDateLessThanEqual(
//                        tenant, startDate, endDate);
//        report.addAll(ReportDto.convertEventList(events));
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<ReportDto>> getReportByDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate date,
                                                           HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        List<Expense> expenses = expenseRepository.findByTenantAndDate(tenant, date);
        List<ReportDto> report = ReportDto.convertExpenseList(expenses);
//        List<Event> events = eventRepository.findByEventDateAndTenant(date, tenant);
//        report.addAll(ReportDto.convertEventList(events));
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<String>> getYearsList(HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        List<LocalDate> expenseDates = expenseRepository.getYearsListByTenant(tenant);
        List<String> years = getYears(expenseDates);
      //  List<LocalDate> eventDates = eventRepository.getYearsListByTenant(tenant);
      //  years.addAll(getYears(eventDates));
        years = years.stream().distinct().collect(Collectors.toList());
        return ResponseEntity.ok(years);
    }

    private List<String> getYears(List<LocalDate> dates) {
        return dates.stream()
                .map(LocalDate::getYear)
                .sorted(Comparator.comparing(Integer::intValue))
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

}
