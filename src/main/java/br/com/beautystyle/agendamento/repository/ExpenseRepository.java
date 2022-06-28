package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Expense;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    @Query("SELECT e.expenseDate FROM Expense e WHERE e.companyId =:tenant")
    List<LocalDate> getYearsList(Long tenant);

    List<Expense> findByCompanyIdEqualsAndExpenseDateGreaterThanEqualAndExpenseDateLessThanEqual(Long id, LocalDate startDate, LocalDate endDate);

    List<Expense> findByCompanyIdAndExpenseDate(Long id, LocalDate date);
}
