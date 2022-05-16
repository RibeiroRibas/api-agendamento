package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.Category;
import br.com.beautystyle.agendamento.model.Expense;
import br.com.beautystyle.agendamento.model.RepeatOrNot;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseForm {


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

    public Expense convert(){
        Expense expense = new Expense();
        expense.setRepeatOrNot(this.repeatOrNot);
        expense.setPrice(this.price);
        expense.setCategory(this.category);
        expense.setDescription(this.description);
        expense.setDate(this.date);
        return expense;
    }
}
