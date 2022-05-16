package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

}
