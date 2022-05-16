package br.com.beautystyle.agendamento.model;

import br.com.beautystyle.agendamento.repository.ExpenseRepository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;
    @Enumerated(EnumType.STRING)
    @NotNull
    private RepeatOrNot repeatOrNot;

    public Expense update(ExpenseRepository expenseRepository) {
        Expense expense = expenseRepository.getOne(this.id);
        expense.setDescription(this.description);
        expense.setCategory(this.category);
        expense.setDate(this.date);
        expense.setPrice(this.price);
        expense.setRepeatOrNot(this.repeatOrNot);
        return expense;
    }

    public void setRepeatOrNot(RepeatOrNot repeatOrNot) {
        this.repeatOrNot = repeatOrNot;
    }

    public Long getId() {
        return id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public RepeatOrNot getRepeatOrNot() {
        return repeatOrNot;
    }
}
