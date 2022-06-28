package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.repository.CategoryRepository;

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
    private Long companyId;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Category update(CategoryRepository categoryRepository) {
        Category category = categoryRepository.getById(this.apiId);
        category.setName(this.name);
        category.setCompanyId(this.companyId);
        return category;
    }
}
