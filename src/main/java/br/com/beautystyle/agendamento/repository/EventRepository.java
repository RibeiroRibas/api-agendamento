package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventDateAndTenant(LocalDate eventDate, Long tenant);

    @Query("SELECT e.eventDate FROM Event e WHERE e.tenant =:tenant")
    List<LocalDate> getYearsListByTenant(Long tenant);

    List<Event> findByTenantEqualsAndEventDateGreaterThanEqualAndEventDateLessThanEqual(Long id, LocalDate startDate, LocalDate endDate);

    List<Event> findByCustomerUserCustomerId(Long userId);
}
