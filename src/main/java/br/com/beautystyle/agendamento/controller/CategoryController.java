package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ClientDto;
import br.com.beautystyle.agendamento.controller.form.ClientForm;
import br.com.beautystyle.agendamento.model.entity.Category;
import br.com.beautystyle.agendamento.model.entity.Client;
import br.com.beautystyle.agendamento.model.entity.Expense;
import br.com.beautystyle.agendamento.repository.CategoryRepository;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    @Cacheable(value = "categories")
    public List<Category> findByCompanyId(@PathVariable Long id) {
        return categoryRepository.findByCompanyId(id);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<Category> insert(@RequestBody @Valid Category category, UriComponentsBuilder uriBuilder) {
        Category savedCategory = categoryRepository.save(category);
        URI uri = uriBuilder.path("/category/{id}")
                .buildAndExpand(savedCategory.getApiId())
                .toUri();
        return ResponseEntity.created(uri).body(savedCategory);
    }

    @PutMapping
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<Category> update(@RequestBody @Valid Category category) {
        Optional<Category> categorytById = categoryRepository.findById(category.getApiId());
        if (categorytById.isPresent()) {
            category = category.update(categoryRepository);
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Category> categoryById = categoryRepository.findById(id);
        if (categoryById.isPresent()) {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
