package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.CompanyDto;
import br.com.beautystyle.agendamento.controller.form.CompanyForm;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public List<CompanyDto> getAll() {
        List<Company> companyList = companyRepository.findAll();
        return CompanyDto.convert(companyList);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CompanyDto> insert(@RequestBody @Valid CompanyForm companyForm, UriComponentsBuilder uriBuilder) {
        Company savedCompany = companyRepository.save(companyForm.converter());
        URI uri = uriBuilder.path("/company/{id}")
                .buildAndExpand(savedCompany.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new CompanyDto(savedCompany));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<CompanyDto> update(@RequestBody @Valid CompanyDto companyDto) {
        Optional<Company> company = companyRepository.findById(companyDto.getApiId());
        if (company.isPresent()) {
            Company update = companyDto.update(companyRepository);
            return ResponseEntity.ok(new CompanyDto(update));
        }
        return ResponseEntity.notFound().build();
    }
}
