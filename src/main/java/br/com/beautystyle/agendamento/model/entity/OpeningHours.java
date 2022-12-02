package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.OpeningHoursForm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class OpeningHours implements Comparable<OpeningHours>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotNull
    private int dayOfWeek;

    public OpeningHours() {
    }

    public OpeningHours(LocalTime startTime, LocalTime endTime, int dayOfWeek) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    public OpeningHours(OpeningHoursForm openingHoursForm) {
        this.startTime = openingHoursForm.getStartTime();
        this.endTime = openingHoursForm.getEndTime();
        this.dayOfWeek = openingHoursForm.getDayOfWeek();
    }

    public static List<OpeningHours> converter(List<OpeningHoursForm> businessHoursForm) {
        return businessHoursForm.stream().map(OpeningHours::new).collect(Collectors.toList());
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public int compareTo(OpeningHours o) {
        if (startTime.equals(o.getStartTime())) return 0;
        if (startTime.isAfter(o.getStartTime())) return 1;
        return -1;
    }
}
