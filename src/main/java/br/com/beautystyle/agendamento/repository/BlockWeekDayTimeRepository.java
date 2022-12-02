package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.BlockWeekDayTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BlockWeekDayTimeRepository extends JpaRepository<BlockWeekDayTime, Long> {

    List<BlockWeekDayTime> findByTenantEqualsAndDayOfWeekEqualsAndEndDateGreaterThanEqual(Long tenant, char dayOfWeek, LocalDate endDate);

}
