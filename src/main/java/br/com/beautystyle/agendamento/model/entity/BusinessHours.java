package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.OpeningHoursForm;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class BusinessHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "business_hours_id")
    @NotNull
    private List<OpeningHours> openingHours;
    @NotNull
    private String description;
    @NotNull
    private int timeInterval;

    public BusinessHours() {
    }

    public BusinessHours(@NotNull @Valid List<OpeningHoursForm> openingHoursForm) {
        this.openingHours = OpeningHours.converter(openingHoursForm);
        this.openingHours = this.openingHours.stream().sorted().collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }
}
