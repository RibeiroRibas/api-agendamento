package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ScheduleByProfessionalDto;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.ScheduleByProfessionalForm;
import br.com.beautystyle.agendamento.model.entity.Customer;
import br.com.beautystyle.agendamento.model.entity.Schedule;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;
import static br.com.beautystyle.agendamento.service.ConstantsService.SCHEDULE;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_ID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ScheduleByProfessionalControllerTest {

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
    private List<Schedule> savedSchedules;
    private MockMvcUtil mockMvc;

    @Before
    public void setup() throws Exception {
        uri = "/event/by_professional/";
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        mockMvc = new MockMvcUtil(mock, mapper);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository, mapper, mock);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        tenant = tokenDto.getTenant();
        token = tokenDto.getToken();
        savedSchedules = registerScheduleUtil.saveSchedules(tenant);
    }

    @Test
    public void giveEventDateWhenFoundEventThenReturnStatusOkAndEventFinalDtoList() throws Exception {
        String uri = this.uri + registerScheduleUtil.getEventDateEqualsWednesday();
        MvcResult mvcResult = mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        ScheduleByProfessionalDto eventFinalDto = mapper.readValue(responseAsString, ScheduleByProfessionalDto.class);
        Assert.assertEquals(2, eventFinalDto.getEvents().size());
    }


    @Test
    public void givenEventByProfessionalFormObjectWhenSavedEventThenReturnStatusCreated() throws Exception {
        ScheduleByProfessionalForm scheduleByProfessionalForm = registerScheduleUtil.initEventByProfessionalFormTest(tenant);
        mockMvc.POST(uri, scheduleByProfessionalForm, token)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenEventByProfessionalFormObjectWhenEventStartTimeIsNotAvailableThenReturnStatusConflict() throws Exception {
        ScheduleByProfessionalForm scheduleByProfessionalForm = registerScheduleUtil.initEventByProfessionalFormTest(tenant);
        scheduleByProfessionalForm.setStartTime(LocalTime.of(7, 0));
        mockMvc.POST(uri, scheduleByProfessionalForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(START_TIME_IS_NOT_AVAILABLE + CAUSED_BY + SCHEDULE));
    }

    @Test
    public void givenEventByProfessionalFormObjectWhenEventDurationTimeIsNotAvailableThenReturnStatusConflict() throws Exception {
        ScheduleByProfessionalForm scheduleByProfessionalForm = registerScheduleUtil.initEventByProfessionalFormTest(tenant);
        scheduleByProfessionalForm.setStartTime(LocalTime.of(6, 30));
        mockMvc.POST(uri, scheduleByProfessionalForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(DURATION_TIME_IS_NOT_AVAILABLE + CAUSED_BY + SCHEDULE));
    }

    @Test
    public void givenEventByProfessionalFormAndEventIdWhenEventUpdatedThenReturnStatusOk() throws Exception {
        Schedule schedule = savedSchedules.stream().findFirst().get();
        String uri = this.uri + schedule.getId();
        ScheduleByProfessionalForm scheduleByProfessionalForm = registerScheduleUtil.initEventByProfessionalFormTest(tenant);
        mockMvc.PUT(uri, scheduleByProfessionalForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void givenInvalidEventIdAndEventByProfessionalFormWhenTryUpdateThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        ScheduleByProfessionalForm scheduleByProfessionalForm = registerScheduleUtil.initEventByProfessionalFormTest(tenant);
        mockMvc.PUT(uri, scheduleByProfessionalForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    @Test
    public void givenEventIdAndEventByProfessionalFormWhenStartTimeNotAvailableThenReturnStatusConflict() throws Exception {
        Schedule schedule = savedSchedules.stream().findFirst().get();
        String uri = this.uri + schedule.getId();
        ScheduleByProfessionalForm scheduleByProfessionalForm = registerScheduleUtil.initEventByProfessionalFormTest(tenant);
        scheduleByProfessionalForm.setStartTime(LocalTime.of(8, 0));
        mockMvc.PUT(uri, scheduleByProfessionalForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(START_TIME_IS_NOT_AVAILABLE + CAUSED_BY + SCHEDULE));
    }

    @Test
    public void givenEventIdAndEventByProfessionalFormWhenTryUpdateAndDurationTimeIsNotAvailableThenReturnStatusConflict() throws Exception {
        Schedule schedule = savedSchedules.stream().findFirst().get();
        String uri = this.uri + schedule.getId();
        ScheduleByProfessionalForm scheduleByProfessionalForm = registerScheduleUtil.initEventByProfessionalFormTest(tenant);
        scheduleByProfessionalForm.setStartTime(LocalTime.of(7, 30));
        mockMvc.PUT(uri, scheduleByProfessionalForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(DURATION_TIME_IS_NOT_AVAILABLE + CAUSED_BY + SCHEDULE));
    }

    @Test
    public void givenEventIdByProfessionalWhenDeletedThenReturnStatusOk() throws Exception {
        Schedule schedule = savedSchedules.stream().findFirst().get();
        String uri = this.uri + schedule.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<Customer> byId = customerRepository.findById(schedule.getCustomer().getId());
        Assert.assertTrue(byId.isPresent());
    }

    @Test
    public void givenInvalidEventIdThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

}
