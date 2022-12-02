package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ReportDto;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
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

import java.util.Arrays;
import java.util.List;

import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReportControllerTest {

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
    private ExpenseRepository expenseRepository;
    private String uri;
    private RegisterScheduleUtil registerScheduleUtil;
    private String token;
    private MockMvcUtil mockMvc;

    @Before
    public void setup() throws Exception {
        uri = "/report/";
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository, expenseRepository);
        mockMvc = new MockMvcUtil(mock, mapper);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository, mapper, mock);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        Long tenant = tokenDto.getTenant();
        token = tokenDto.getToken();
        registerScheduleUtil.saveSchedules(tenant);
        for (int i = 1; true; i++) {
            if (i == 3) {
                registerScheduleUtil.saveExpenses(i, INVALID_TENANT);
                break;
            }
            registerScheduleUtil.saveExpenses(i, tenant);
        }
    }

    @Test
    public void shouldReturnReportByPeriod() throws Exception {
        String uri = this.uri + registerScheduleUtil.getEventDateEqualsWednesday() +
                "/" + registerScheduleUtil.getEventDateEqualsWednesday().plusDays(9);
        MvcResult mvcResult = this.mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<ReportDto> report = Arrays.asList(mapper.readValue(responseAsString, ReportDto[].class));
        Assert.assertEquals(5, report.size());
    }

    @Test
    public void shouldReturnReportByDate() throws Exception {
        String uri = this.uri + registerScheduleUtil.getEventDateEqualsWednesday();
        MvcResult mvcResult = this.mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<ReportDto> report = Arrays.asList(mapper.readValue(responseAsString, ReportDto[].class));
        Assert.assertEquals(2, report.size());
    }

    @Test
    public void shouldReturnYearsList() throws Exception {
        MvcResult mvcResult = this.mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<String> report = Arrays.asList(mapper.readValue(responseAsString, String[].class));
        Assert.assertEquals(1, report.size());
    }


}
