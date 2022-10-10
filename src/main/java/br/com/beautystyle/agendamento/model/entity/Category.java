package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.CategoryForm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apiId;
    @NotNull
    private String name;
    @NotNull
    private Long tenant;

    public Category() {
    }

    public Category(String name, Long tenant) {
        this.name = name;
        this.tenant = tenant;
    }

    public Category(CategoryForm categoryForm) {
        this.name = categoryForm.getName();
        this.tenant = categoryForm.getTenant();
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }


    public boolean isTenantNotEquals(Long tenant) {
        return !this.tenant.equals(tenant);
    }
}
