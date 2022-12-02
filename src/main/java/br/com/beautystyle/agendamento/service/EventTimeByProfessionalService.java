package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.exceptions.EventDateNotAvailableException;
import br.com.beautystyle.agendamento.controller.exceptions.EventTimeNotAvailableException;
import br.com.beautystyle.agendamento.controller.exceptions.RequestNotAllowException;
import br.com.beautystyle.agendamento.controller.form.EventAvailableTimesForm;
import br.com.beautystyle.agendamento.model.EventAvailableTimes;
import br.com.beautystyle.agendamento.model.ResponseHandler;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.util.EventTimeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;
import static br.com.beautystyle.agendamento.service.ConstantsService.*;

@Service
public class EventTimeByProfessionalService {

    @Autowired
    private EventTimeComponent eventTimeComponent;

    private List<Schedule> schedules;
    private List<BlockTimeOnDay> blockTimesOnDay;
    private List<BlockTimeEveryDay> blockTimesEveryDay;
    private List<BlockWeekDayTime> blockWeekDayTimes;

    public ResponseEntity<?> checkEventTimeIsAvailable(EventTime eventTime, LocalDate date) {

        schedules = eventTimeComponent.getSchedules(date, eventTime.getTenant());
        blockTimesOnDay = eventTimeComponent.getBlockTimesOnDay(date, eventTime.getTenant());
        blockTimesEveryDay = eventTimeComponent.getBlockTimesEveryDay(date, eventTime.getTenant());
        blockWeekDayTimes = eventTimeComponent.getBlockWeekDayTimes(date, eventTime.getTenant());

        Map<String, List<EventTime>> eventsMap = new HashMap<>();
        eventsMap.put(SCHEDULE, new ArrayList<>(schedules));
        eventsMap.put(BLOCK_TIME_ON_DAY, new ArrayList<>(blockTimesOnDay));
        eventsMap.put(BLOCK_TIME_EVERY_DAY, new ArrayList<>(blockTimesEveryDay));
        eventsMap.put(BLOCK_WEEK_DAY_TIME, new ArrayList<>(blockWeekDayTimes));

        for (Map.Entry<String, List<EventTime>> entry : eventsMap.entrySet()) {

            Optional<EventTime> optionalEventTime = eventTime.isStartTimeNotAvailable(entry.getValue());
            if (optionalEventTime.isPresent()) {
                Object event = getEventByKey(entry.getKey(), optionalEventTime.get().getId());
                return ResponseHandler.generateResponseWithData(
                        START_TIME_IS_NOT_AVAILABLE + CAUSED_BY + entry.getKey(),
                        HttpStatus.CONFLICT, event);
            }

            optionalEventTime = eventTime.isDurationTimeNotAvailable(entry.getValue());
            if (optionalEventTime.isPresent()) {
                Object event = getEventByKey(entry.getKey(), optionalEventTime.get().getId());
                return ResponseHandler.generateResponseWithData(
                        DURATION_TIME_IS_NOT_AVAILABLE + CAUSED_BY + entry.getKey(),
                        HttpStatus.CONFLICT, event);
            }

        }

        return null;
    }

    private Object getEventByKey(String key, Long id) {
        if (key.equals(SCHEDULE))
            return schedules.stream().map(schedule -> schedule.getId().equals(id)).findFirst().get();
        if (key.equals(BLOCK_TIME_ON_DAY))
            return blockTimesOnDay.stream().map(blockTime -> blockTime.getId().equals(id)).findFirst().get();
        if (key.equals(BLOCK_WEEK_DAY_TIME))
            return blockWeekDayTimes.stream().map(blockTime -> blockTime.getId().equals(id)).findFirst().get();
        if (key.equals(BLOCK_TIME_EVERY_DAY))
            return blockTimesEveryDay.stream().map(blockTime -> blockTime.getId().equals(id)).findFirst().get();
        return null;
    }

}
