package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.EventTime;
import br.com.beautystyle.agendamento.util.EventTimeComponent;
import br.com.beautystyle.agendamento.util.RegisterBlockTimeUtil;
import br.com.beautystyle.agendamento.util.RegisterScheduleUtil;
import br.com.beautystyle.agendamento.util.RegisterUserUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventTimeRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private BlockTimeOnDayRepository blockTimeOnDayRepository;
    @Autowired
    private BlockTimeEveryDayRepository blockTimeEveryDayRepository;
    @Autowired
    private BlockWeekDayTimeRepository blockWeekDayTimeRepository;
    @Autowired
    private EventTimeComponent eventTimeComponent;

    private Long tenant;
    private RegisterScheduleUtil registerScheduleUtil;
    private RegisterBlockTimeUtil registerBlockTimeUtil;

    @Before
    public void setup() {
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        registerBlockTimeUtil = new RegisterBlockTimeUtil(blockTimeEveryDayRepository, blockTimeOnDayRepository, blockWeekDayTimeRepository);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository);
        tenant = registerUserUtil.saveUserProfessional().getCompany().getId();
        registerScheduleUtil.saveSchedules(tenant);
        registerBlockTimeUtil.saveAllBlockTimesOnDay(tenant);
        registerBlockTimeUtil.saveAllBlockTimesEveryDay(tenant);
        registerBlockTimeUtil.saveAllBlockWeekDayTimes(tenant);
    }

    @Test
    public void shouldReturnEventList() {
        List<EventTime> eventTimes = new ArrayList<>();
        eventTimes.addAll(eventTimeComponent.getSchedules(registerScheduleUtil.getEventDateEqualsWednesday(), tenant));
        eventTimes.addAll(eventTimeComponent.getBlockTimesEveryDay(registerScheduleUtil.getEventDateEqualsWednesday(), tenant));
        eventTimes.addAll(eventTimeComponent.getBlockTimesOnDay(registerScheduleUtil.getEventDateEqualsWednesday(), tenant));
        eventTimes.addAll(eventTimeComponent.getBlockWeekDayTimes(registerScheduleUtil.getEventDateEqualsWednesday(),tenant));
        System.out.println(eventTimes);
        Assert.assertEquals(6, eventTimes.size());
    }
}
