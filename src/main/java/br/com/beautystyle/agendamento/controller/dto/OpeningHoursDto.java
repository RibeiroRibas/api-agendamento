package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.OpeningHours;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class OpeningHoursDto {

    private LocalTime startTime;
    private LocalTime endTime;
    private int dayOfWeek;
    private Long apiId;

    public OpeningHoursDto() {
    }

    public OpeningHoursDto(OpeningHours openingHours) {
        this.startTime = openingHours.getStartTime();
        this.endTime = openingHours.getEndTime();
        this.dayOfWeek = openingHours.getDayOfWeek();
        this.apiId = openingHours.getId();
    }

    public static List<OpeningHoursDto> converter(List<OpeningHours> businessHours) {
        return businessHours.stream().map(OpeningHoursDto::new).collect(Collectors.toList());
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
}
