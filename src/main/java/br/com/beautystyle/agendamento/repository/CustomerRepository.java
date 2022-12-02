package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByTenant(Long tenant);
}
