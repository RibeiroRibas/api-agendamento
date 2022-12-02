package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
