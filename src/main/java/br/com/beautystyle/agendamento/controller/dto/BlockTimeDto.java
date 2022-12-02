package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.BlockTimeEveryDay;
import br.com.beautystyle.agendamento.model.entity.BlockTimeOnDay;
import br.com.beautystyle.agendamento.model.entity.BlockWeekDayTime;

import java.util.List;

public class BlockTimeDto {

    private List<BlockWeekDayTime> blockWeekDayTimes;
    private List<BlockTimeOnDay> blockTimesOnDay;
    private List<BlockTimeEveryDay> blockTimesEveryDay;

    public BlockTimeDto() {
    }

    public List<BlockWeekDayTime> getBlockWeekDayTimes() {
        return blockWeekDayTimes;
    }

    public void setBlockWeekDayTimes(List<BlockWeekDayTime> blockWeekDayTimes) {
        this.blockWeekDayTimes = blockWeekDayTimes;
    }

    public List<BlockTimeOnDay> getBlockTimesOnDay() {
        return blockTimesOnDay;
    }

    public void setBlockTimesOnDay(List<BlockTimeOnDay> blockTimesOnDay) {
        this.blockTimesOnDay = blockTimesOnDay;
    }

    public List<BlockTimeEveryDay> getBlockTimesEveryDay() {
        return blockTimesEveryDay;
    }

    public void setBlockTimesEveryDay(List<BlockTimeEveryDay> blockTimesEveryDay) {
        this.blockTimesEveryDay = blockTimesEveryDay;
    }
}
