package br.com.beautystyle.agendamento.model;

import br.com.beautystyle.agendamento.controller.dto.EventTimeDto;
import br.com.beautystyle.agendamento.model.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventAvailableTimes {

    private final List<OpeningHours> openingHours;
    private final List<EventTime> eventTimes;
    private final int interval;

    public EventAvailableTimes(List<EventTime> eventTimes,BusinessHours businessHours) {
        this.eventTimes = eventTimes;
        this.openingHours = businessHours.getOpeningHours();
        this.interval = businessHours.getTimeInterval();
    }

    public List<EventTimeDto> getAvailableTimesDto(LocalTime durationTime, LocalDate eventDate) {
        List<EventTimeDto> eventTimesAvailable = new ArrayList<>();
        List<OpeningHours> openingHours = getOpeningHourByDayOfWeek(eventDate);
        for (OpeningHours openingHour : openingHours) {
            LocalTime startTime = openingHour.getStartTime();
            do {
                LocalTime endTime = startTime.plusHours(durationTime.getHour()).plusMinutes(durationTime.getMinute());
                boolean isTimeAvailable = !endTime.isAfter(openingHour.getEndTime());

                EventTime eventTime = new Schedule(startTime, endTime);

                if(eventTime.isEventTimeNotAvailable(eventTimes))
                    isTimeAvailable = false;

                if (isTimeAvailable)
                    eventTimesAvailable.add(new EventTimeDto(eventTime));

                startTime = startTime.plusMinutes(interval);

            } while (startTime.isBefore(openingHour.getEndTime()));
        }
        return eventTimesAvailable;
    }

    private List<OpeningHours> getOpeningHourByDayOfWeek(LocalDate eventDate) {
        return openingHours.stream()
                .filter(openingHour -> openingHour.getDayOfWeek() == eventDate.getDayOfWeek().getValue())
                .sorted()
                .collect(Collectors.toList());
    }
}
