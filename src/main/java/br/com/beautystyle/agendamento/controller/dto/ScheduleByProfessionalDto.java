package br.com.beautystyle.agendamento.controller.dto;

import java.util.List;

public class ScheduleByProfessionalDto {

    private List<EventDto> events;
    private BlockTimeDto blockTimes;

    public ScheduleByProfessionalDto() {
    }

    public ScheduleByProfessionalDto(BlockTimeDto blockTimes, List<EventDto> events) {
        this.events = events;
        this.blockTimes = blockTimes;
    }

    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }

    public BlockTimeDto getBlockTimes() {
        return blockTimes;
    }

    public void setBlockTimes(BlockTimeDto blockTimes) {
        this.blockTimes = blockTimes;
    }

}
