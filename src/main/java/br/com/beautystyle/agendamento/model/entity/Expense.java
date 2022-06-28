package br.com.beautystyle.agendamento.model.entity;

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
    private BigDecimal price;
    @NotNull
    private LocalDate expenseDate;
    @NotNull
    private String category;
    @Enumerated(EnumType.STRING)
    @NotNull
    private RepeatOrNot repeatOrNot;
    @NotNull
    private Long companyId;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
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

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
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

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public RepeatOrNot getRepeatOrNot() {
        return repeatOrNot;
    }
}
