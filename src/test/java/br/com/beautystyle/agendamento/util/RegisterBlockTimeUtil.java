package br.com.beautystyle.agendamento.util;

import br.com.beautystyle.agendamento.controller.form.BlockTimeOnDayForm;
import br.com.beautystyle.agendamento.model.entity.BlockTimeEveryDay;
import br.com.beautystyle.agendamento.model.entity.BlockTimeOnDay;
import br.com.beautystyle.agendamento.model.entity.BlockWeekDayTime;
import br.com.beautystyle.agendamento.repository.BlockTimeEveryDayRepository;
import br.com.beautystyle.agendamento.repository.BlockTimeOnDayRepository;
import br.com.beautystyle.agendamento.repository.BlockWeekDayTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

public class RegisterBlockTimeUtil {

    private BlockTimeEveryDayRepository blockTimeEveryDayRepository;
    private BlockTimeOnDayRepository blockTimeOnDayRepository;
    private BlockWeekDayTimeRepository blockWeekDayTimeRepository;

    public RegisterBlockTimeUtil(BlockTimeEveryDayRepository blockTimeEveryDayRepository) {
        this.blockTimeEveryDayRepository = blockTimeEveryDayRepository;
        this.blockTimeEveryDayRepository.deleteAll();
    }

    public RegisterBlockTimeUtil(BlockTimeOnDayRepository blockTimeOnDayRepository) {
        this.blockTimeOnDayRepository = blockTimeOnDayRepository;
        this.blockTimeOnDayRepository.deleteAll();
    }

    public RegisterBlockTimeUtil(BlockWeekDayTimeRepository blockWeekDayTimeRepository) {
        this.blockWeekDayTimeRepository = blockWeekDayTimeRepository;
        this.blockWeekDayTimeRepository.deleteAll();
    }

    public RegisterBlockTimeUtil(BlockTimeEveryDayRepository blockTimeEveryDayRepository,
                                 BlockTimeOnDayRepository blockTimeOnDayRepository,
                                 BlockWeekDayTimeRepository blockWeekDayTimeRepository) {
        this.blockTimeEveryDayRepository = blockTimeEveryDayRepository;
        this.blockTimeEveryDayRepository.deleteAll();
        this.blockTimeOnDayRepository = blockTimeOnDayRepository;
        this.blockTimeOnDayRepository.deleteAll();
        this.blockWeekDayTimeRepository = blockWeekDayTimeRepository;
        this.blockWeekDayTimeRepository.deleteAll();
    }


    public void saveAllBlockTimesEveryDay(Long tenant) {
        List<BlockTimeEveryDay> blockTimes = new ArrayList<>();
        for (int i = 9; i < 13; i++) {
            BlockTimeEveryDay blockTime = initBlockTimeEveryDay(tenant);
            blockTime.setStartTime(LocalTime.of(6 + i, 0));
            blockTime.setEndTime(LocalTime.of(7 + i, 0));
            if (i == 10) {
                List<LocalDate> exceptionDates = new ArrayList<>();
                exceptionDates.add(getEventDateEqualsWednesday());
                blockTime.setExceptionDates(exceptionDates);
            }
            if (i == 11)
                blockTime.setEndDate(getEventDateEqualsWednesday().minusDays(14));
            if (i == 12)
                blockTime.setTenant(INVALID_TENANT);
            blockTimes.add(blockTime);
        }
        blockTimeEveryDayRepository.saveAll(blockTimes);
    }

    private BlockTimeEveryDay initBlockTimeEveryDay(Long tenant) {
        BlockTimeEveryDay blockTime = new BlockTimeEveryDay();
        blockTime.setReason("NO HAVE REASON");
        blockTime.setTenant(tenant);
        blockTime.setDate(getEventDateEqualsWednesday());
        blockTime.setEndDate(getEventDateEqualsWednesday().plusDays(7));
        return blockTime;
    }

    public LocalDate getEventDateEqualsWednesday() {
        LocalDate eventDate = LocalDate.now();
        while (eventDate.getDayOfWeek().getValue() != 3)
            eventDate = eventDate.plusDays(1);
        return eventDate.plusDays(7);
    }

    public void saveAllBlockTimesOnDay(Long tenant) {
        List<BlockTimeOnDay> blockTimes = new ArrayList<>();
        for (int i = 5; i < 9; i++) {
            BlockTimeOnDay blockTime = initBlockTimeOnDay(tenant, i);
            if (i == 7)
                blockTime.setDate(getEventDateEqualsWednesday().minusDays(7));
            if (i == 8)
                blockTime.setTenant(INVALID_TENANT);
            blockTimes.add(blockTime);
        }
        blockTimeOnDayRepository.saveAll(blockTimes);
    }

    private BlockTimeOnDay initBlockTimeOnDay(Long tenant, int i) {
        BlockTimeOnDay blockTime = new BlockTimeOnDay();
        blockTime.setReason("NO HAVE REASON");
        blockTime.setTenant(tenant);
        blockTime.setStartTime(LocalTime.of(6 + i, 0));
        blockTime.setEndTime(LocalTime.of(7 + i, 0));
        blockTime.setDate(getEventDateEqualsWednesday());
        return blockTime;
    }

    public void saveAllBlockWeekDayTimes(Long tenant) {
        List<BlockWeekDayTime> blockTimes = new ArrayList<>();
        for (int i = 13; i < 18; i++) {
            BlockWeekDayTime blockTime = initBlockTimeWeekDayTime(tenant);
            blockTime.setStartTime(LocalTime.of(6 + i, 0));
            blockTime.setEndTime(blockTime.getStartTime().plusMinutes(30));
            if (i == 13)
                blockTime.setDayOfWeek('2');
            if (i == 14)
                blockTime.setEndDate(getEventDateEqualsWednesday().minusDays(14));
            if (i == 15)
                blockTime.setTenant(INVALID_TENANT);
            if (i == 16) {
                List<LocalDate> exceptionDates = new ArrayList<>();
                exceptionDates.add(getEventDateEqualsWednesday());
                blockTime.setExceptionDates(exceptionDates);
            }
            blockTimes.add(blockTime);
        }
        blockWeekDayTimeRepository.saveAll(blockTimes);
    }

    private BlockWeekDayTime initBlockTimeWeekDayTime(Long tenant) {
        BlockWeekDayTime blockTime = new BlockWeekDayTime();
        blockTime.setReason("NO HAVE REASON");
        blockTime.setTenant(tenant);
        blockTime.setDate(getEventDateEqualsWednesday());
        blockTime.setEndDate(getEventDateEqualsWednesday().plusDays(7));
        blockTime.setDayOfWeek('3');
        return blockTime;
    }

    public BlockTimeOnDay saveBlockTimeOnDay(Long tenant) {
        BlockTimeOnDay blockTime = new BlockTimeOnDay();
        blockTime.setDate(getEventDateEqualsWednesday());
        blockTime.setStartTime(LocalTime.of(12, 0));
        blockTime.setEndTime(LocalTime.of(17, 0));
        blockTime.setReason("Commitment");
        blockTime.setTenant(tenant);
        return blockTimeOnDayRepository.save(blockTime);
    }

    public BlockTimeOnDayForm initBlockTimeOnDayForm() {
        BlockTimeOnDayForm blockTimeOnDayForm = new BlockTimeOnDayForm();
        blockTimeOnDayForm.setDate(getEventDateEqualsWednesday());
        blockTimeOnDayForm.setStartTime(LocalTime.of(12, 0));
        blockTimeOnDayForm.setEndTime(LocalTime.of(17, 0));
        blockTimeOnDayForm.setReason("Commitment");
        return blockTimeOnDayForm;
    }
}
