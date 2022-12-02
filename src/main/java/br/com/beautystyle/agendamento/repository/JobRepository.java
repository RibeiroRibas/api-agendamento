package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface JobRepository extends JpaRepository<Job, Long> {

    Set<Job> findByTenant(Long id);


}
