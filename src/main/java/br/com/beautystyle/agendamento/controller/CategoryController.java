package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.exceptions.TenantNotEqualsException;
import br.com.beautystyle.agendamento.controller.form.CategoryForm;
import br.com.beautystyle.agendamento.model.entity.Category;
import br.com.beautystyle.agendamento.repository.CategoryRepository;
import javax.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TokenServices tokenServices;

    @GetMapping
    @Cacheable(value = "categories")
    public List<Category> findByTenant(HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        return categoryRepository.findByTenant(tenant);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<Category> insert(@RequestBody @Valid CategoryForm categoryForm,
                                           HttpServletRequest request,
                                           UriComponentsBuilder uriBuilder) {
        categoryForm.setTenant(tokenServices.getTenant(request));
        Category savedCategory = categoryRepository.save(new Category(categoryForm));
        URI uri = uriBuilder.path("/category/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedCategory);
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<Category> update(@PathVariable Long id,
                                           @RequestBody @Valid CategoryForm categoryForm,
                                           HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            if (optionalCategory.get().isTenantNotEquals(tenant))
                throw new TenantNotEqualsException(TENANT_NOT_EQUALS);
            Category category = categoryForm.update(id, categoryRepository);
            return ResponseEntity.ok(category);
        }
        throw new EntityNotFoundException(CATEGORY_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            if (optionalCategory.get().isTenantNotEquals(tenant))
                throw new TenantNotEqualsException(TENANT_NOT_EQUALS);
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException(CATEGORY_NOT_FOUND);
    }
}
