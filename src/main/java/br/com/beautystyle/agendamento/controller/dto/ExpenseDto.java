package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.model.RepeatOrNot;
import br.com.beautystyle.agendamento.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseDto {

    private Long apiId;
    private String description;
    private BigDecimal value;
    private LocalDate expenseDate;
    private String category;
    private RepeatOrNot repeatOrNot;
    private Long tenant;

    public ExpenseDto() {
    }

    public ExpenseDto(Expense savedExpense) {
        this.apiId = savedExpense.getId();
        this.description = savedExpense.getDescription();
        this.value = savedExpense.getValue();
        this.expenseDate = savedExpense.getExpenseDate();
        this.category = savedExpense.getCategory();
        this.repeatOrNot = savedExpense.getRepeatOrNot();
        this.tenant = savedExpense.getTenant();
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRepeatOrNot(RepeatOrNot repeatOrNot) {
        this.repeatOrNot = repeatOrNot;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
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


    public RepeatOrNot getRepeatOrNot() {
        return repeatOrNot;
    }

    public static List<ExpenseDto> convert(List<Expense> expenseList) {
        return expenseList.stream().map(ExpenseDto::new).collect(Collectors.toList());
    }

    public Expense convert(){
        Expense expense = new Expense();
        expense.setRepeatOrNot(this.repeatOrNot);
        expense.setValue(this.value);
        expense.setCategory(this.category);
        expense.setDescription(this.description);
        expense.setExpenseDate(this.expenseDate);
        expense.setTenant(this.tenant);
        return expense;
    }

    public Expense update(ExpenseRepository expenseRepository) {
            Expense expense = expenseRepository.getById(this.apiId);
            expense.setDescription(this.description);
            expense.setCategory(this.category);
            expense.setExpenseDate(this.expenseDate);
            expense.setValue(this.value);
            expense.setRepeatOrNot(this.repeatOrNot);
            return expense;
    }
}
