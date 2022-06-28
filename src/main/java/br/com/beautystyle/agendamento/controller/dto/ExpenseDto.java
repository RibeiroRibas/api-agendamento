package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.model.RepeatOrNot;
import br.com.beautystyle.agendamento.repository.ExpenseRepository;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseDto {


    private Long apiId;
    private String description;
    @NotNull
    private BigDecimal price;
    @NotNull
    private LocalDate expenseDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private String category;
    @Enumerated(EnumType.STRING)
    @NotNull
    private RepeatOrNot repeatOrNot;
    private Long companyId;

    public ExpenseDto(Expense savedExpense) {
        this.apiId = savedExpense.getId();
        this.description = savedExpense.getDescription();
        this.price = savedExpense.getPrice();
        this.expenseDate = savedExpense.getExpenseDate();
        this.category = savedExpense.getCategory();
        this.repeatOrNot = savedExpense.getRepeatOrNot();
        this.companyId = savedExpense.getCompanyId();
    }

    public static List<ExpenseDto> convert(List<Expense> expenseList) {
        return expenseList.stream().map(ExpenseDto::new).collect(Collectors.toList());
    }

    public ExpenseDto() {
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

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
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
        expense.setExpenseDate(this.expenseDate);
        expense.setCompanyId(this.companyId);
        return expense;
    }

    public Expense update(ExpenseRepository expenseRepository) {
            Expense expense = expenseRepository.getById(this.apiId);
            expense.setDescription(this.description);
            expense.setCategory(this.category);
            expense.setExpenseDate(this.expenseDate);
            expense.setPrice(this.price);
            expense.setRepeatOrNot(this.repeatOrNot);
            return expense;
    }
}
