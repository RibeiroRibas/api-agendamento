package br.com.beautystyle.agendamento.util;

import br.com.beautystyle.agendamento.controller.dto.BlockTimeDto;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.repository.BlockTimeEveryDayRepository;
import br.com.beautystyle.agendamento.repository.BlockTimeOnDayRepository;
import br.com.beautystyle.agendamento.repository.BlockWeekDayTimeRepository;
import br.com.beautystyle.agendamento.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.beautystyle.agendamento.service.ConstantsService.*;

@Component
public class EventTimeComponent {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private BlockTimeOnDayRepository blockTimeOnDayRepository;
    @Autowired
    private BlockTimeEveryDayRepository blockTimeEveryDayRepository;
    @Autowired
    private BlockWeekDayTimeRepository blockWeekDayTimeRepository;

    public List<BlockTimeOnDay> getBlockTimesOnDay(LocalDate date, Long tenant) {
        return blockTimeOnDayRepository.findByDateAndTenant(date, tenant);
    }

    public List<BlockTimeEveryDay> getBlockTimesEveryDay(LocalDate date, Long tenant) {
        return blockTimeEveryDayRepository.findByEndDateGreaterThanEqualAndTenantEquals(date, tenant)
                .stream()
                .filter(blockTimeEveryDay -> blockTimeEveryDay
                        .getExceptionDates()
                        .stream()
                        .noneMatch(exceptionDate -> exceptionDate.equals(date)))
                .collect(Collectors.toList());
    }

    public List<BlockWeekDayTime> getBlockWeekDayTimes(LocalDate date, Long tenant) {
        char dayOfWeek = Character.highSurrogate(date.getDayOfWeek().getValue());
        return blockWeekDayTimeRepository.findByTenantEqualsAndDayOfWeekEqualsAndEndDateGreaterThanEqual(tenant, dayOfWeek, date)
                .stream()
                .filter(blockTimeEveryDay -> blockTimeEveryDay
                        .getExceptionDates()
                        .stream()
                        .noneMatch(exceptionDate -> exceptionDate.equals(date)))
                .collect(Collectors.toList());
    }

    public List<Schedule> getSchedules(LocalDate date, Long tenant) {
        return scheduleRepository.findByDateAndTenant(date, tenant);
    }

    public List<EventTime> getEventTimes(LocalDate date, Long tenant) {
        List<EventTime> eventTimes = new ArrayList<>();
        eventTimes.addAll(getBlockTimesEveryDay(date, tenant));
        eventTimes.addAll(getBlockTimesOnDay(date, tenant));
        eventTimes.addAll(getBlockWeekDayTimes(date, tenant));
        eventTimes.addAll(getSchedules(date, tenant));
        return eventTimes;
    }

    public Map<String, List<EventTime>> getEventsMap(LocalDate date, Long tenant) {
        Map<String, List<EventTime>> events = new HashMap<>();
        events.put(BLOCK_TIME_EVERY_DAY, new ArrayList<>(getBlockTimesEveryDay(date, tenant)));
        events.put(BLOCK_TIME_ON_DAY, new ArrayList<>(getBlockTimesOnDay(date, tenant)));
        events.put(BLOCK_WEEK_DAY_TIME, new ArrayList<>(getBlockWeekDayTimes(date, tenant)));
        events.put(SCHEDULE, new ArrayList<>(getSchedules(date, tenant)));
        return events;
    }

    public BlockTimeDto getBlockTimesDto(LocalDate date, Long tenant) {
        List<BlockTimeEveryDay> blockTimesEveryDay = getBlockTimesEveryDay(date, tenant);
        List<BlockWeekDayTime> blockWeekDayTimes = getBlockWeekDayTimes(date, tenant);
        List<BlockTimeOnDay> blockTimesOnDay = getBlockTimesOnDay(date, tenant);
        BlockTimeDto blockTimeDto = new BlockTimeDto();
        blockTimeDto.setBlockTimesOnDay(blockTimesOnDay);
        blockTimeDto.setBlockTimesEveryDay(blockTimesEveryDay);
        blockTimeDto.setBlockWeekDayTimes(blockWeekDayTimes);
        return blockTimeDto;
    }
}
