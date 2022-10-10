package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {

}
