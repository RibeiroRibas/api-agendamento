package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    @Query("SELECT e.date FROM Expense e WHERE e.tenant =:tenant")
    List<LocalDate> getYearsListByTenant(Long tenant);

    List<Expense> findByTenantEqualsAndDateGreaterThanEqualAndDateLessThanEqual(Long tenant, LocalDate startDate, LocalDate endDate);

    List<Expense> findByTenantAndDate(Long id, LocalDate date);
}
