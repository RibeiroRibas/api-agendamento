package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.Event;
import br.com.beautystyle.agendamento.model.StatusPagamento;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository repository;

    @Autowired
    private TestEntityManager entityManager;

  //  @Test
    void deveriaCarregarUmAgendamentoAoBuscarPorUmaDataEspecifica() {
        LocalDate eventDate = LocalDate.of(2022, 4, 18);
        Event event = new Event();
        event.setEventId(1L);
        event.setEventDate(eventDate);
        event.setValueEvent(new BigDecimal("22.0"));
        event.setEndTime(LocalTime.of(18, 0));
        event.setStarTime(LocalTime.of(17, 0));
        event.setStatusPagamento(StatusPagamento.NAORECEBIDO);
//        event.setClient(new Client("Moreira","48996417323"));
//        event.setServiceList(Collections.singletonList(new Service("pintar a unha", new BigDecimal("22.0"), LocalTime.of(1, 0))));
        entityManager.persist(event);
//
//        Event findedEvent = repository.findByEventDate(eventDate);
//        assertEquals(eventDate, findedEvent.getEventDate());
    }

}