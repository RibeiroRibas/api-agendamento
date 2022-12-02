package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDateAndTenant(LocalDate eventDate, Long tenant);

    List<Schedule> findByCustomerUserCustomerId(Long userId);

    List<Schedule> findByCustomerId(Long id);
}
