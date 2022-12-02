package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.BlockTimeEveryDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BlockTimeEveryDayRepository extends JpaRepository<BlockTimeEveryDay,Long> {

    List<BlockTimeEveryDay> findByEndDateGreaterThanEqualAndTenantEquals(LocalDate date, Long tenant);

}
