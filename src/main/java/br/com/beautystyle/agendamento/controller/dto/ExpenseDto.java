package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Expense;

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
    private boolean repeat;
    private Long tenant;

    public ExpenseDto() {
    }

    public ExpenseDto(Expense savedExpense) {
        this.apiId = savedExpense.getId();
        this.description = savedExpense.getDescription();
        this.value = savedExpense.getValue();
        this.expenseDate = savedExpense.getDate();
        this.category = savedExpense.getCategory().getName();
        this.repeat = savedExpense.isRepeat();
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

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public static List<ExpenseDto> convert(List<Expense> expenseList) {
        return expenseList.stream().map(ExpenseDto::new).collect(Collectors.toList());
    }

}
