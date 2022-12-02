package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.BlockTimeOnDayForm;
import br.com.beautystyle.agendamento.model.entity.BlockTimeOnDay;
import br.com.beautystyle.agendamento.repository.*;
import br.com.beautystyle.agendamento.util.MockMvcUtil;
import br.com.beautystyle.agendamento.util.RegisterBlockTimeUtil;
import br.com.beautystyle.agendamento.util.RegisterScheduleUtil;
import br.com.beautystyle.agendamento.util.RegisterUserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;
import static br.com.beautystyle.agendamento.service.ConstantsService.BLOCK_TIME_ON_DAY;
import static br.com.beautystyle.agendamento.service.ConstantsService.SCHEDULE;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_ID;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BlockTimeOnDayControllerTest {

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
    @Autowired
    private BlockTimeOnDayRepository blockTimeRepository;
    private String uri;
    private Long tenant;
    private String token;
    private MockMvcUtil mockMvc;
    private RegisterBlockTimeUtil registerBlockTimeUtil;

    @Before
    public void setup() throws Exception {
        uri = "/block_time_on_day/";
        mockMvc = new MockMvcUtil(mock, mapper);
        RegisterScheduleUtil registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository, userRepository, mapper, mock);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        tenant = tokenDto.getTenant();
        token = tokenDto.getToken();
        registerScheduleUtil.saveSchedules(tenant);
        registerBlockTimeUtil = new RegisterBlockTimeUtil(blockTimeRepository);
    }

    @Test
    public void givenBlockTimeFormObjectWhenSavedThenReturnStatusCreated() throws Exception {
        BlockTimeOnDayForm blockTimeOnDayForm = registerBlockTimeUtil.initBlockTimeOnDayForm();
        mockMvc.POST(this.uri, blockTimeOnDayForm, token)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenBlockTimeFormObjectWhenStartTimeNotAvailableThenReturnStatusConflict() throws Exception {
        registerBlockTimeUtil.saveBlockTimeOnDay(tenant);
        BlockTimeOnDayForm blockTimeOnDayForm = registerBlockTimeUtil.initBlockTimeOnDayForm();
        blockTimeOnDayForm.setStartTime(LocalTime.of(13, 0));
        blockTimeOnDayForm.setEndTime(LocalTime.of(14, 0));
        mockMvc.POST(this.uri, blockTimeOnDayForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(START_TIME_IS_NOT_AVAILABLE + CAUSED_BY + BLOCK_TIME_ON_DAY));
    }

    @Test
    public void givenBlockTimeFormObjectWhenDurationTimeNotAvailableThenReturnStatusConflict() throws Exception {
        registerBlockTimeUtil.saveBlockTimeOnDay(tenant);
        BlockTimeOnDayForm blockTimeOnDayForm = registerBlockTimeUtil.initBlockTimeOnDayForm();
        blockTimeOnDayForm.setStartTime(LocalTime.of(11, 30));
        blockTimeOnDayForm.setEndTime(LocalTime.of(12, 30));
        mockMvc.POST(this.uri, blockTimeOnDayForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(DURATION_TIME_IS_NOT_AVAILABLE + CAUSED_BY + BLOCK_TIME_ON_DAY));
    }

    @Test
    public void givenBlockTimeFormObjectWhenUpdatedThenReturnStatusOk() throws Exception {
        BlockTimeOnDay blockTime = registerBlockTimeUtil.saveBlockTimeOnDay(tenant);
        BlockTimeOnDayForm blockTimeOnDayForm = registerBlockTimeUtil.initBlockTimeOnDayForm();
        blockTimeOnDayForm.setStartTime(LocalTime.of(11, 30));
        blockTimeOnDayForm.setEndTime(LocalTime.of(12, 0));
        String uri = this.uri + blockTime.getId();
        mockMvc.PUT(uri, blockTimeOnDayForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidIdAndBlockTimeFormObjectThenReturnStatusNotFound() throws Exception {
        BlockTimeOnDayForm blockTimeOnDayForm = registerBlockTimeUtil.initBlockTimeOnDayForm();
        String uri = this.uri + INVALID_ID;
        mockMvc.PUT(uri, blockTimeOnDayForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    @Test
    public void givenBlockTimeFormObjectWhenToTryUpdateAndStartTimeNotAvailableThenReturnStatusNotAcceptable() throws Exception {
        BlockTimeOnDay blockTimeOnDay = registerBlockTimeUtil.saveBlockTimeOnDay(tenant);
        BlockTimeOnDayForm blockTimeOnDayForm = registerBlockTimeUtil.initBlockTimeOnDayForm();
        blockTimeOnDayForm.setStartTime(LocalTime.of(8, 0));
        blockTimeOnDayForm.setEndTime(LocalTime.of(9, 0));
        String uri = this.uri + blockTimeOnDay.getId();
        mockMvc.PUT(uri, blockTimeOnDayForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(START_TIME_IS_NOT_AVAILABLE + CAUSED_BY + SCHEDULE));
    }

    @Test
    public void givenBlockTimeFormObjectWhenTryUpdateAndDurationTimeNotAvailableThenReturnStatusConflict() throws Exception {
        BlockTimeOnDay blockTimeOnDay = registerBlockTimeUtil.saveBlockTimeOnDay(tenant);
        BlockTimeOnDayForm blockTimeOnDayForm = registerBlockTimeUtil.initBlockTimeOnDayForm();
        blockTimeOnDayForm.setStartTime(LocalTime.of(6, 0));
        blockTimeOnDayForm.setEndTime(LocalTime.of(8, 0));
        String uri = this.uri + blockTimeOnDay.getId();
        mockMvc.PUT(uri, blockTimeOnDayForm, token)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(DURATION_TIME_IS_NOT_AVAILABLE + CAUSED_BY + SCHEDULE));
    }

    @Test
    public void givenBlockTimeIdWhenDeletedThenReturnStatusOk() throws Exception {
        BlockTimeOnDay blockTime = registerBlockTimeUtil.saveBlockTimeOnDay(tenant);
        String uri = this.uri + blockTime.getId();
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidBlockTimeIdThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        mockMvc.DELETE(uri, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

}
