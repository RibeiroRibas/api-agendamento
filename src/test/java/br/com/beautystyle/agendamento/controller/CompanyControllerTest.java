package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.CompanyForm;
import br.com.beautystyle.agendamento.controller.form.OpeningHoursForm;
import br.com.beautystyle.agendamento.model.entity.Address;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.model.entity.UserProfessional;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import br.com.beautystyle.agendamento.repository.ProfileRepository;
import br.com.beautystyle.agendamento.repository.UserRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CompanyControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    private String token;
    private UserProfessional userProfessional;


    @Before
    public void setup() throws Exception {
     //   eventRepository.deleteAll();
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository, mapper, mockMvc);
        userProfessional = registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        token = tokenDto.getToken();
    }

    @Test
    public void givenCompanyFormObjectWhenUpdateCompanyTheReturnStatusOk() throws Exception {
        CompanyForm companyForm = initCompanyForm();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/company")
                        .content(mapper.writeValueAsString(companyForm))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<Company> optionalCompany = companyRepository.findById(userProfessional.getCompany().getId());
        if (optionalCompany.isPresent()) {
            Company savedCompany = userProfessional.getCompany();
            Company updatedCompany = optionalCompany.get();
            Assert.assertNotEquals(savedCompany.getName(), updatedCompany.getName());
            Assert.assertNotEquals(savedCompany.getRn(), updatedCompany.getRn());
            Assert.assertNotEquals(savedCompany.getAddress().getCity(), updatedCompany.getAddress().getCity());
            Assert.assertNotEquals(savedCompany.getAddress().getCountry(), updatedCompany.getAddress().getCountry());
            Assert.assertNotEquals(savedCompany.getAddress().getNumber(), updatedCompany.getAddress().getNumber());
            Assert.assertNotEquals(savedCompany.getAddress().getState(), updatedCompany.getAddress().getState());
            Assert.assertNotEquals(savedCompany.getAddress().getStreet(), updatedCompany.getAddress().getStreet());
            Assert.assertNotEquals(savedCompany.getAddress().getZipCode(), updatedCompany.getAddress().getZipCode());
        }
    }

    private CompanyForm initCompanyForm() {
        CompanyForm companyForm = new CompanyForm();
        companyForm.setAddress(initAddress());
        companyForm.setName("Test Company 2");
        companyForm.setRn("Test Registered Number 2");
        companyForm.setDescription(" businessHours.setDescription(\"De Terça á Sexta das 8:00 ás 12:00 e das 13:30 ás 19:00. Sábado das 8:00 ás 17:00\");");
        List<OpeningHoursForm> openingHoursForm = new ArrayList<>();
        for (int day = 2; day <= 5; day++) {
            openingHoursForm.add(new OpeningHoursForm(LocalTime.of(8, 0), LocalTime.of(12, 0), day));
            openingHoursForm.add(new OpeningHoursForm(LocalTime.of(13, 30), LocalTime.of(19, 0), day));
        }
        openingHoursForm.add(new OpeningHoursForm(LocalTime.of(8, 0), LocalTime.of(17, 0), 6));
        companyForm.setOpeningHours(openingHoursForm);
        return companyForm;
    }

    private Address initAddress() {
        Address address = new Address();
        address.setCity("Test City 2");
        address.setCountry("Test Country 2");
        address.setNumber(20);
        address.setState("Test State 2");
        address.setZipCode("Test Cip Code 2");
        address.setStreet("Test Street 2");
        return address;
    }
}
