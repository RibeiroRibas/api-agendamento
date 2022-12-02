package br.com.beautystyle.agendamento.model;

import br.com.beautystyle.agendamento.model.entity.EventTime;
import br.com.beautystyle.agendamento.model.entity.OpeningHours;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TimeAvailable {

    boolean isEventTimeNotAvailable(List<EventTime> eventTimes);

    Optional<EventTime> isStartTimeNotAvailable(List<EventTime> eventTimes);

    Optional<EventTime> isDurationTimeNotAvailable(List<EventTime> eventTimes);

}
