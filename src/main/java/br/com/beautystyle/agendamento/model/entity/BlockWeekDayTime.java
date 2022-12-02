package br.com.beautystyle.agendamento.model.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table(indexes = @Index(columnList = "tenant"))
public class BlockWeekDayTime extends BlockTimeChild {

    @NotNull
    private Character dayOfWeek;

    public BlockWeekDayTime() {
        super();
    }

    public BlockWeekDayTime(Long id, LocalTime startTime, LocalTime endTime) {
        super(id, startTime, endTime);
    }

    public Character getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Character dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}
