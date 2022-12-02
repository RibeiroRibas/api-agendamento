package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.BlockTimeOnDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BlockTimeOnDayRepository extends JpaRepository<BlockTimeOnDay, Long> {

    List<BlockTimeOnDay> findByDateAndTenant (LocalDate date, Long tenant);

}
