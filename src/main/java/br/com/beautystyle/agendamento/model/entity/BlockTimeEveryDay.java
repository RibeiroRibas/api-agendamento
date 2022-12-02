package br.com.beautystyle.agendamento.model.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = @Index(columnList = "tenant"))
public class BlockTimeEveryDay extends BlockTimeChild {

    public BlockTimeEveryDay() {
        super();
    }

    public BlockTimeEveryDay(Long id, LocalTime startTime, LocalTime endTime) {
        super(id, startTime, endTime);
    }

}
