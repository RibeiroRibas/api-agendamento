package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.entity.OpeningHours;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class OpeningHoursForm {

    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private int dayOfWeek;

    public OpeningHoursForm() {
    }

    public OpeningHoursForm(LocalTime startTime, LocalTime endTime, int dayOfWeek) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    public OpeningHoursForm(OpeningHours openingHours) {
        this.startTime = openingHours.getStartTime();
        this.endTime = openingHours.getEndTime();
        this.dayOfWeek = openingHours.getDayOfWeek();
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

}
