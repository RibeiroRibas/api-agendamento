package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.JobByCostumerDto;
import br.com.beautystyle.agendamento.controller.dto.JobDto;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.JobForm;
import br.com.beautystyle.agendamento.model.entity.Job;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_ID;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JobControllerTest {

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
    private String tokenUserProfessional;
    private List<Schedule> schedules;
    private String tokenUserCostumer;
    private MockMvcUtil mockMvc;

    @Before
    public void setup() throws Exception {
        uri = "/job/";
        mockMvc = new MockMvcUtil(mock, mapper);
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository, userRepository, mapper, mock);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        tenant = tokenDto.getTenant();
        tokenUserProfessional = tokenDto.getToken();
        registerUserUtil.saveUserCostumer();
        tokenUserCostumer = registerUserUtil.authUserCostumer();
        schedules = registerScheduleUtil.saveSchedules(tenant);
    }

    @Test
    public void shouldReturnJobDtoList() throws Exception {
        registerScheduleUtil.saveJobTest(INVALID_TENANT);
        MvcResult mvcResult = mockMvc.GET(uri, tokenUserProfessional);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<JobDto> savedJobs = Arrays.asList(mapper.readValue(responseAsString, JobDto[].class));
        Assert.assertEquals(schedules.size(), savedJobs.size());
    }

    @Test
    public void givenTenantWhenFindJobsByTenantThenReturnAvailableJobDtoList() throws Exception {
        registerScheduleUtil.saveJobTest(INVALID_TENANT);
        String uri = this.uri + "/available/" + tenant;
        MvcResult mvcResult = mockMvc.GET(uri, tokenUserCostumer);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<JobByCostumerDto> response = Arrays.asList(mapper.readValue(responseAsString, JobByCostumerDto[].class));
        Assert.assertEquals(schedules.size(), response.size());
    }

    @Test
    public void givenInvalidTenantThenReturnStatusNotFound() throws Exception {
        mock.perform(MockMvcRequestBuilders
                        .get(uri + "available/" + INVALID_TENANT)
                        .header("Authorization", "Bearer " + tokenUserCostumer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(COMPANY_NOT_FOUND));
    }

    @Test
    public void givenJobFormObjectWhenSavedThenReturnStatusOk() throws Exception {
        JobForm jobForm = registerScheduleUtil.initJobForm();
        mockMvc.POST(uri, jobForm, tokenUserProfessional)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void givenJobIdAndJobFormObjectWhenUpdatedThenReturnStatusOk() throws Exception {
        Set<Job> jobs = registerScheduleUtil.saveJobTest(tenant);
        Job job = jobs.stream().findFirst().get();
        JobForm jobForm = registerScheduleUtil.initJobForm();
        String uri = this.uri + job.getId();
        mockMvc.PUT(uri, jobForm, tokenUserProfessional)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(jobForm.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(jobForm.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.durationTime").value(
                        jobForm.getDurationTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }

    @Test
    public void givenInvalidJobIdAndJobFormObjectThenReturnStatusNotFound() throws Exception {
        JobForm jobForm = registerScheduleUtil.initJobForm();
        String uri = this.uri + INVALID_ID;
        mockMvc.PUT(uri, jobForm, tokenUserProfessional)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(JOB_NOT_FOUND));
    }

    @Test
    public void givenJobIdFromAnotherUserAndJobFormObjectThenReturnStatusBadRequest() throws Exception {
        Set<Job> jobs = registerScheduleUtil.saveJobTest(INVALID_TENANT);
        Job job = jobs.stream().findFirst().get();
        JobForm jobForm = registerScheduleUtil.initJobForm();
        String uri = this.uri + job.getId();
        mockMvc.PUT(uri, jobForm, tokenUserProfessional)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TENANT_NOT_EQUALS));
    }

    @Test
    public void givenJobIdWhenDeletedJobThenReturnStatusOkAndRemoveEventAssociation() throws Exception {
        Schedule foundSchedule = schedules.stream().findFirst().get();
        Job savedJob = foundSchedule.getJobs().stream().findFirst().get();
        String uri = this.uri + savedJob.getId();
        mockMvc.DELETE(uri, tokenUserProfessional)
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<Schedule> schedule = scheduleRepository.findById(foundSchedule.getId());
        Assert.assertTrue(schedule.isPresent());
        Assert.assertFalse(schedule.get().getJobs().stream().anyMatch(job -> job.getId().equals(savedJob.getId())));
    }

    @Test
    public void givenInvalidJobIdThenReturnStatusNotFound() throws Exception {
        String uri = this.uri + INVALID_ID;
        mockMvc.DELETE(uri, tokenUserProfessional)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(JOB_NOT_FOUND));
    }

    @Test
    public void givenJobIdFromAnotherUserThenReturnStatusBadRequest() throws Exception {
        Set<Job> jobs = registerScheduleUtil.saveJobTest(INVALID_TENANT);
        Job job = jobs.stream().findFirst().get();
        String uri = this.uri + job.getId();
        mockMvc.DELETE(uri, tokenUserProfessional)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TENANT_NOT_EQUALS));
    }

}
