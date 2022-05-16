package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {

}
