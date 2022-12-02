package br.com.beautystyle.agendamento.model.entity;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class BlockTimeChild  extends BlockTime{

    @NotNull
    private LocalDate endDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "blockTimeEveryDay_id")
    private List<LocalDate> exceptionDates = new ArrayList<>();

    public BlockTimeChild() {
    }

    public BlockTimeChild(Long id, LocalTime startTime, LocalTime endTime) {
        super(id, startTime, endTime);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<LocalDate> getExceptionDates() {
        return exceptionDates;
    }

    public void setExceptionDates(List<LocalDate> exceptionDates) {
        this.exceptionDates = exceptionDates;
    }
}
