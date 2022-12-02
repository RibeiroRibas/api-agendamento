package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.entity.Category;
import br.com.beautystyle.agendamento.repository.CategoryRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryForm {

    @NotNull
    private String name;
    @JsonIgnore
    private Long tenant;

    public static List<Category> convert(List<CategoryForm> categories) {
        return categories.stream().map(Category::new).collect(Collectors.toList());
    }

    public CategoryForm(String name) {
        this.name = name;
    }

    public CategoryForm() {
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category update(Long id, CategoryRepository categoryRepository) {
        Category category = categoryRepository.getById(id);
        category.setName(this.name);
        return category;
    }

}
