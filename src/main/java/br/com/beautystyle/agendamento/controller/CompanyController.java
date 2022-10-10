package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.CompanyDto;
import br.com.beautystyle.agendamento.controller.form.CompanyForm;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private TokenServices tokenServices;

    @GetMapping("/available")
    public List<CompanyDto> findAll() {
        List<Company> companies = companyRepository.findAll();
        return CompanyDto.convert(companies);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid CompanyForm companyForm,
                                    HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        companyForm.update(companyRepository, tenant);
        return ResponseEntity.ok().build();
    }

}
