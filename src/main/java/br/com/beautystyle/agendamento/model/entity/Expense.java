package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.ExpenseForm;
import br.com.beautystyle.agendamento.model.RepeatOrNot;

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
    private BigDecimal value;
    @NotNull
    private LocalDate expenseDate;
    @NotNull
    private String category;
    @Enumerated(EnumType.STRING)
    @NotNull
    private RepeatOrNot repeatOrNot;
    @NotNull
    private Long tenant;

    public Expense() {
    }

    public Expense(ExpenseForm expenseForm) {
        this.description = expenseForm.getDescription();
        this.value = expenseForm.getValue();
        this.expenseDate = expenseForm.getExpenseDate();
        this.category = expenseForm.getCategory();
        this.repeatOrNot = expenseForm.getRepeatOrNot();
        this.tenant = expenseForm.getTenant();
    }

    public void setRepeatOrNot(RepeatOrNot repeatOrNot) {
        this.repeatOrNot = repeatOrNot;
    }

    public Long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTenant(Long companyId) {
        this.tenant = companyId;
    }

    public Long getTenant() {
        return tenant;
    }

    public RepeatOrNot getRepeatOrNot() {
        return repeatOrNot;
    }

    public boolean isTenantNotEquals(Long tenant) {
        return !this.tenant.equals(tenant);
    }
}
