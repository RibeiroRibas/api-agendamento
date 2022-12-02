package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.CustomerDto;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.CustomerForm;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.CUSTOMER_NOT_FOUND;
import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_ID;
import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {

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
    private String token;
    private List<Schedule> schedules;
    private MockMvcUtil mockMvc;

    @Before
    public void setup() throws Exception {
        uri = "/customer";
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        mockMvc = new MockMvcUtil(mock, mapper);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository, userRepository, mapper, mock);
        registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        Long tenant = tokenDto.getTenant();
        token = tokenDto.getToken();
        schedules = registerScheduleUtil.saveSchedules(tenant);
    }

    @Test
    public void shouldReturnStatusOkAndCostumerDtoList() throws Exception {
        registerScheduleUtil.saveCostumerTest(INVALID_TENANT);
        MvcResult mvcResult = mockMvc.GET(uri, token);
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<CustomerDto> savedCostumers = Arrays.asList(mapper.readValue(responseAsString, CustomerDto[].class));
        Assert.assertEquals(schedules.size(), savedCostumers.size());
    }

    @Test
    public void givenCostumerFormObjectWhenSavedThenReturnStatusCreatedAndCostumerDtoObject() throws Exception {
        CustomerForm customerForm = initCostumerForm();
        mockMvc.POST(uri, customerForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenCostumerFormListObjectWhenSavedThenReturnStatusOKAndCostumerListDto() throws Exception {
        List<CustomerForm> costumers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            costumers.add(initCostumerForm());
        }
        MvcResult mvcResult = mockMvc.POST(uri + "/insert_all", costumers, token)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseAsString = mvcResult.getResponse().getContentAsString();
        List<CustomerDto> savedCostumers = Arrays.asList(mapper.readValue(responseAsString, CustomerDto[].class));
        Assert.assertEquals(costumers.size(), savedCostumers.size());

    }

    @Test
    public void givenCostumerIdAndCostumerFormObjectWhenUpdatedThenReturnStatusOkAndCostumerDtoObject() throws Exception {
        Customer savedCustomer = schedules.stream().findFirst().get().getCustomer();
        CustomerForm customerForm = initCostumerForm();
        mockMvc.PUT(uri + "/" + savedCustomer.getId(), customerForm, token)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenInvalidCostumerIdAndCostumerFormObjectWhenTryUpdateThenReturnStatusNotFound() throws Exception {
        CustomerForm customerForm = initCostumerForm();
        mockMvc.PUT(uri + "/" + INVALID_ID, customerForm, token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    @Test
    public void givenCostumerIdWhenDeletedCostumerThenReturnStatusOkAndRemoveEventAssociation() throws Exception {
        Schedule foundSchedule = schedules.stream().findFirst().get();
        Customer customer = foundSchedule.getCustomer();
        mockMvc.DELETE(uri + "/" + customer.getId(), token)
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<Schedule> schedule = scheduleRepository.findById(foundSchedule.getId());
        Assert.assertTrue(schedule.isPresent());
        Assert.assertEquals(INVALID_ID,schedule.get().getCustomer().getId());
    }

    @Test
    public void givenInvalidCostumerIdWhenTryDeleteThenReturnStatusNotFound() throws Exception {
        mockMvc.DELETE(uri + "/" + INVALID_ID,token)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ENTITY_NOT_FOUND));
    }

    private CustomerForm initCostumerForm() {
        CustomerForm customerForm = new CustomerForm();
        customerForm.setName("Test Name 2");
        customerForm.setPhone("Test Phone 2");
        return customerForm;
    }

}
