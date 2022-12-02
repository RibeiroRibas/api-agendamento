package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.form.LoginForm;
import br.com.beautystyle.agendamento.repository.ProfileRepository;
import br.com.beautystyle.agendamento.repository.UserRepository;
import br.com.beautystyle.agendamento.util.MockMvcUtil;
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

import static br.com.beautystyle.agendamento.controller.ConstantsController.BAD_CREDENTIALS;
import static br.com.beautystyle.agendamento.util.ConstantsTest.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mock;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;
    private RegisterUserUtil registerUserUtil;
    private MockMvcUtil mockMvc;

    @Before
    public void init() {
      //  eventRepository.deleteAll();
        registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository);
        mockMvc = new MockMvcUtil(mock, mapper);
    }

    @Test
    public void givenLoginFormObjectWhenUserAuthenticateIsIncorrectThenReturnStatusBadRequest() throws Exception {
        LoginForm loginForm = new LoginForm(INVALID_EMAIL, USER_PASSWORD);
        mockMvc.POST("/auth", loginForm)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(BAD_CREDENTIALS));
    }

    @Test
    public void givenLoginFormObjectWhenUserProfessionalAuthenticateIsOkThenReturnStatusOkAndTokenDtoObject() throws Exception {
        Long savedProfessionalTenant = registerUserUtil.saveUserProfessional().getCompany().getId();
        LoginForm loginForm = new LoginForm(PROFESSIONAL_EMAIL, USER_PASSWORD);
        mockMvc.POST("/auth", loginForm)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.profiles").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tenant").value(savedProfessionalTenant))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Bearer"));
    }

    @Test
    public void givenUserLoginObjectWhenUserCostumerAuthenticateIsOkThenReturnStatusOkAndTokenDtoObject() throws Exception {
        registerUserUtil.saveUserCostumer();
        LoginForm loginForm = new LoginForm(CUSTOMER_EMAIL, USER_PASSWORD);
        mockMvc.POST("/auth", loginForm)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Bearer"));
    }

}
