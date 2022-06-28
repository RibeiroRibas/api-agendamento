package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Client;
import br.com.beautystyle.agendamento.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventDateAndCompanyId(LocalDate eventDate, Long companyId);

    List<Event> findByClientClientId(Long clientId);

    @Query("SELECT e.eventDate FROM Event e WHERE e.companyId =:tenant")
    List<LocalDate> getYearsList(Long tenant);

    List<Event> findByCompanyIdEqualsAndEventDateGreaterThanEqualAndEventDateLessThanEqual(Long id, LocalDate startDate, LocalDate endDate);

}
