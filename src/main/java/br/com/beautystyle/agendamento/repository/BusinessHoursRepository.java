package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BusinessHoursRepository extends JpaRepository<BusinessHours,Long> {
}
