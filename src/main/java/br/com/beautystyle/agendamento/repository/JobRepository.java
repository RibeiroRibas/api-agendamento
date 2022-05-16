package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job,Long> {
}
