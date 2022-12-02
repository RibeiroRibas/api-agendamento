package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.ExpenseForm;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(indexes = @Index(columnList = "tenant"))
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @NotNull
    private BigDecimal value;
    @NotNull
    private LocalDate date;
    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @NotNull
    private Category category;
    @Column(name = "isRepeat", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean repeat;
    @NotNull
    private Long tenant;

    public Expense() {
    }

    public Expense(ExpenseForm expenseForm) {
        this.description = expenseForm.getDescription();
        this.value = expenseForm.getValue();
        this.date = expenseForm.getExpenseDate();
        this.category = new Category(expenseForm.getCategoryId());
        this.repeat = expenseForm.isRepeatOrNot();
        this.tenant = expenseForm.getTenant();
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

    public void setDate(LocalDate date) {
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setTenant(Long companyId) {
        this.tenant = companyId;
    }

    public Long getTenant() {
        return tenant;
    }

    public boolean isTenantNotEquals(Long tenant) {
        return !this.tenant.equals(tenant);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
}
