package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.exceptions.EntityNotFoundException;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company findById(Long id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND)
        );
    }

}
