package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ScheduleByCostumerDto;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.EventAvailableTimesForm;
import br.com.beautystyle.agendamento.controller.form.ScheduleByCostumerForm;
import br.com.beautystyle.agendamento.model.entity.Schedule;
import br.com.beautystyle.agendamento.model.entity.UserCustomer;
import br.com.beautystyle.agendamento.repository.*;
import br.com.beautystyle.agendamento.util.MockMvcUtil;
import br.com.beautystyle.agendamento.util.RegisterScheduleUtil;
import br.com.beautystyle.agendamento.util.RegisterUserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_ID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ScheduleByCustomerControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mock;
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

    private String uri;
    private RegisterScheduleUtil registerScheduleUtil;
    private Long tenant;
    private String token;
    private UserCustomer userCustomer;
    private MockMvcUtil mockMvc;

    @Before
    public void setup() throws Exception {
        uri = "/event/by_customer/";
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        mockMvc = new MockMvcUtil(mock, mapper);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository, mapper, mock);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        tenant = tokenDto.getTenant();
        registerScheduleUtil.saveSchedules(tenant);
        userCustomer = registerUserUtil.saveUserCostumer();
        token = registerUserUtil.authUserCostumer();
    }

    @Test
    public void shouldReturnStatusOkAndEventListObjectScheduledByCostumer() throws Exception {
        registerScheduleUtil.saveScheduleWithUserCostumer(userCustomer, tenant);
        MvcResult mvcResult = mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<ScheduleByCostumerDto> scheduleByCostumerDto = Arrays.asList(mapper.readValue(responseAsString, ScheduleByCostumerDto[].class));
        Assert.assertEquals(1, scheduleByCostumerDto.size());
    }

    @Test
    public void givenEventByCostumerFormObjectWhenSavedEventThenReturnStatusOk() throws Exception {
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        mockMvc.POST(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenEventByCostumerFormObjectWhenEventDateIsNotAvailableThenReturnStatusNotAcceptable() throws Exception {
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        LocalDate eventDate = LocalDate.now();
        while (eventDate.getDayOfWeek().getValue() != 7)
            eventDate = eventDate.plusDays(1);
        scheduleByCostumerForm.setEventDate(eventDate);
        mockMvc.POST(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EVENT_DATE_NOT_AVAILABLE));
    }

    @Test
    public void givenEventByCostumerFormObjectWhenEventStartTimeIsNotAvailableThenReturnStatusNotAcceptable() throws Exception {
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        scheduleByCostumerForm.setStartTime(LocalTime.of(7, 0));
        mockMvc.POST(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EVENT_TIME_IS_NOT_AVAILABLE));

    }

    @Test
    public void givenEventByCostumerFormObjectWhenEventDurationTimeIsNotAvailableThenReturnStatusNotAcceptable() throws Exception {
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        scheduleByCostumerForm.setStartTime(LocalTime.of(6, 30));
        mockMvc.POST(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EVENT_TIME_IS_NOT_AVAILABLE));
    }

    @Test
    public void givenEventAvailableTimesFormObjectWhenCheckAvailableTimesThenReturnStatusOkAndEventAvailableTimesDtoObject() throws Exception {
        String uri = this.uri + "/available_time";
        registerScheduleUtil.saveScheduleWithUserCostumer(userCustomer, tenant);
        Schedule schedule = registerScheduleUtil.initSchedule(tenant);
        schedule.setStartTime(LocalTime.of(15, 30));
        schedule.setEndTime(LocalTime.of(16, 30));
        scheduleRepository.save(schedule);
        EventAvailableTimesForm eventAvailableTimesForm = registerScheduleUtil.initEventAvailableTimesForm(tenant);
        String expected = "[{\"startTime\":\"09:00:00\",\"endTime\":\"10:00:00\"},{\"startTime\":\"09:30:00\",\"endTime\":\"10:30:00\"},{\"startTime\":\"10:00:00\",\"endTime\":\"11:00:00\"},{\"startTime\":\"13:30:00\",\"endTime\":\"14:30:00\"},{\"startTime\":\"14:00:00\",\"endTime\":\"15:00:00\"},{\"startTime\":\"14:30:00\",\"endTime\":\"15:30:00\"},{\"startTime\":\"16:30:00\",\"endTime\":\"17:30:00\"},{\"startTime\":\"17:00:00\",\"endTime\":\"18:00:00\"},{\"startTime\":\"17:30:00\",\"endTime\":\"18:30:00\"},{\"startTime\":\"18:00:00\",\"endTime\":\"19:00:00\"}]";
        MvcResult mvcResult = mockMvc.POST(uri, eventAvailableTimesForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(responseAsString);
        Assert.assertEquals(expected, responseAsString);
    }

    @Test
    public void givenEventByCostumerFormAndEventIdWhenEventUpdatedThenReturnStatusOk() throws Exception {
        Schedule schedule = registerScheduleUtil.saveScheduleWithUserCostumer(userCustomer, tenant);
        String uri = this.uri + schedule.getId();
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        mockMvc.PUT(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(schedule.getId());
        if (scheduleOptional.isPresent()) {
            Schedule updateSchedule = scheduleOptional.get();
            Assert.assertEquals(scheduleByCostumerForm.getEventDate(), updateSchedule.getDate());
            Assert.assertEquals(scheduleByCostumerForm.getStartTime(), updateSchedule.getStartTime());
        }
    }

    @Test
    public void givenInvalidEventIdAndEventByCostumerFormWhenTryUpdateThenReturnStatusNotFound() throws Exception {
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        String uri = this.uri + INVALID_ID;
        mockMvc.PUT(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    @Test
    public void givenEventIdAndEventByCostumerFormObjectWhenTryUpdateAndEventStartingIn12hrsOrLessThenReturnStatusNotAcceptable() throws Exception {
        Schedule schedule = registerScheduleUtil.initSchedule(tenant);
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(12);
        schedule.setDate(localDateTime.toLocalDate());
        schedule.setStartTime(localDateTime.toLocalTime());
        schedule.setEndTime(localDateTime.toLocalTime().plusHours(1));
        schedule.setCustomer(userCustomer.getCustomer());
        scheduleRepository.save(schedule);
        String uri = this.uri + schedule.getId();
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        scheduleByCostumerForm.setStartTime(localDateTime.toLocalTime().plusHours(1));
        mockMvc.PUT(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(UPDATE_OR_DELETE_NOT_ALLOW));
    }

    @Test
    public void givenEventIdAndEventByCostumerFormObjectWhenTryUpdateAndEventDateNotAvailableThenReturnStatusNotAcceptable() throws Exception {
        Schedule schedule = registerScheduleUtil.saveScheduleWithUserCostumer(userCustomer, tenant);
        String uri = this.uri + schedule.getId();
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        scheduleByCostumerForm.setEventDate(registerScheduleUtil.getEventDateEqualsWednesday().plusDays(4));
        mockMvc.PUT(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EVENT_DATE_NOT_AVAILABLE));
    }

    @Test
    public void givenEventIdAndEventByCostumerFormObjectWhenTryUpdateAndStartTimeIsNotAvailableThenReturnStatusNotAcceptable() throws Exception {
        Schedule schedule = registerScheduleUtil.saveScheduleWithUserCostumer(userCustomer, tenant);
        String uri = this.uri + schedule.getId();
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        scheduleByCostumerForm.setStartTime(LocalTime.of(7, 0));
        mockMvc.PUT(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EVENT_TIME_IS_NOT_AVAILABLE));
    }

    @Test
    public void givenEventIdAndEventByCostumerFormObjectWhenTryUpdateAndEventDurationTimeIsNotAvailableThenReturnStatusConflict() throws Exception {
        Schedule schedule = registerScheduleUtil.saveScheduleWithUserCostumer(userCustomer, tenant);
        String uri = this.uri + schedule.getId();
        ScheduleByCostumerForm scheduleByCostumerForm = registerScheduleUtil.initEventByCostumerForm(tenant);
        scheduleByCostumerForm.setStartTime(LocalTime.of(6, 30));
        mockMvc.PUT(uri, scheduleByCostumerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EVENT_TIME_IS_NOT_AVAILABLE));
    }

    @Test
    public void givenEventIdWhenDeletedThenReturnStatusOk() throws Exception {
        Schedule schedule = registerScheduleUtil.saveScheduleWithUserCostumer(userCustomer, tenant);
        String uri = this.uri + schedule.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidEventIdThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    @Test
    public void givenEventIdWhenTryEventStartingIn12hrsOrLessThenReturnStatusNotAcceptable() throws Exception {
        Schedule schedule = registerScheduleUtil.initSchedule(tenant);
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(12);
        schedule.setDate(localDateTime.toLocalDate());
        schedule.setStartTime(localDateTime.toLocalTime());
        schedule.setEndTime(localDateTime.toLocalTime().plusHours(1));
        schedule.setCustomer(userCustomer.getCustomer());
        scheduleRepository.save(schedule);
        String uri = this.uri + schedule.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(UPDATE_OR_DELETE_NOT_ALLOW));
    }
}
