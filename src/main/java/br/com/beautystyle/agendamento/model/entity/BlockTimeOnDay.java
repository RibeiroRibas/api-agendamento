package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.controller.form.BlockTimeOnDayForm;
import br.com.beautystyle.agendamento.repository.BlockTimeOnDayRepository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(indexes = @Index(columnList = "tenant"))
public class BlockTimeOnDay extends BlockTime {

    public BlockTimeOnDay() {
        super();
    }

    public BlockTimeOnDay(Long id, LocalTime startTime, LocalTime endTime) {
        super(id, startTime, endTime);
    }

    public BlockTimeOnDay(BlockTimeOnDayForm blockTimeOnDayForm, Long tenant) {
        super(blockTimeOnDayForm.getStartTime(),
                blockTimeOnDayForm.getEndTime(),
                blockTimeOnDayForm.getDate(),
                blockTimeOnDayForm.getReason(),
                tenant);
    }

    public void update(BlockTimeOnDay blockTime) {
        blockTime.setEndTime(getEndTime());
        blockTime.setStartTime(getStartTime());
        blockTime.setDate(getDate());
        blockTime.setReason(getReason());
    }
}
