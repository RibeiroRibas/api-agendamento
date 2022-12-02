package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.*;
import br.com.beautystyle.agendamento.model.entity.*;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mock;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private BusinessHoursRepository businessHoursRepository;

    private String uri;
    private RegisterUserUtil registerUserUtil;
    private String tokenProfessional;
    private UserProfessional userProfessional;
    private UserCustomer userCustomer;
    private String tokenCostumer;
    private Long tenant;
    private MockMvcUtil mockMvc;
    private RegisterScheduleUtil registerScheduleUtil;

    @Before
    public void setup() throws Exception {
        uri = "/user/";
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository, mapper, mock);
        mockMvc = new MockMvcUtil(mock, mapper);
        userCustomer = registerUserUtil.saveUserCostumer();
        tokenCostumer = registerUserUtil.authUserCostumer();
        userProfessional = registerUserUtil.saveUserProfessional();
        TokenDto tokenDto = registerUserUtil.authUserProfessional();
        tokenProfessional = tokenDto.getToken();
        tenant = tokenDto.getTenant();
    }

    @Test
    public void givenUserFormObjectAndCompanyFormObjectWhenCreateUserProfessionalThenReturnStatusOk() throws Exception {
        Address address = registerUserUtil.initAddress();
        CompanyForm companyForm = initCompanyForm(address);
        UserForm userForm = initUser(address);
        UserProfessionalForm userProfessionalForm = new UserProfessionalForm(userForm, companyForm);
        String uri = this.uri + "professional";
        mockMvc.POST(uri, userProfessionalForm, tokenProfessional)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenUserFormObjectWhenCreateUserCostumerThenReturnStatusOk() throws Exception {
        Address address = registerUserUtil.initAddress();
        UserForm userForm = initUser(address);
        String uri = this.uri + "customer";
        mockMvc.POST(uri, userForm, tokenCostumer)
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void givenUserObjectWhenUpdateUserThenReturnStatusOk() throws Exception {
        Address address = initAddress();
        UserForm userForm = initUser(address);
        mockMvc.PUT(uri, userForm, tokenProfessional)
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<User> optionalUser = userRepository.findById(userProfessional.getId());
        if (optionalUser.isPresent()) {
            User updatedUser = optionalUser.get();
            Assert.assertNotEquals(userProfessional.getName(), updatedUser.getName());
            Assert.assertNotEquals(userProfessional.getPassword(), updatedUser.getPassword());
            Assert.assertNotEquals(userProfessional.getEmail(), updatedUser.getEmail());
            Assert.assertNotEquals(userProfessional.getPhone(), updatedUser.getPhone());
            Assert.assertNotEquals(userProfessional.getAddress().getCity(), updatedUser.getAddress().getCity());
            Assert.assertNotEquals(userProfessional.getAddress().getCountry(), updatedUser.getAddress().getCountry());
            Assert.assertNotEquals(userProfessional.getAddress().getNumber(), updatedUser.getAddress().getNumber());
            Assert.assertNotEquals(userProfessional.getAddress().getState(), updatedUser.getAddress().getState());
            Assert.assertNotEquals(userProfessional.getAddress().getStreet(), updatedUser.getAddress().getStreet());
            Assert.assertNotEquals(userProfessional.getAddress().getZipCode(), updatedUser.getAddress().getZipCode());
        }

    }

    @Test
    public void whenDeleteUserProfessionalThenReturnStatusOkAndDeleteUserAddressAndCompanyAndCompanyAddressAndBusinessHours() throws Exception {
        String uri = this.uri + "professional";
        mockMvc.DELETE(uri, tokenProfessional)
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<User> optionalUser = userRepository.findById(userProfessional.getId());
        Assert.assertFalse(optionalUser.isPresent());
        Optional<Address> userAddress = addressRepository.findById(userProfessional.getAddress().getId());
        Assert.assertFalse(userAddress.isPresent());
        Optional<Company> company = companyRepository.findById(userProfessional.getCompany().getId());
        Assert.assertFalse(company.isPresent());
        Optional<Address> companyAddress = addressRepository.findById(userProfessional.getAddress().getId());
        Assert.assertFalse(companyAddress.isPresent());
        Optional<BusinessHours> businessHours = businessHoursRepository.findById(userProfessional.getCompany().getBusinessHours().getId());
        Assert.assertFalse(businessHours.isPresent());

    }

//    @Test
//    public void whenDeleteUserCostumerThenReturnStatusOkAndDeleteUserAddressAndCostumerAndEventAssociation() throws Exception {
//        Event event1 = registerEventUtil.saveEventWithUserCostumer(userCustomer, tenant);
//        Event event2 = registerEventUtil.saveEventWithUserCostumer(userCustomer, tenant);
//        String uri = this.uri + "customer";
//        mockMvc.DELETE(uri, tokenCostumer)
//                .andExpect(MockMvcResultMatchers.status().is(200));
//        Optional<User> optionalUser = userRepository.findById(userCustomer.getId());
//        Assert.assertFalse(optionalUser.isPresent());
//        Optional<Address> userAddress = addressRepository.findById(userCustomer.getAddress().getId());
//        Assert.assertFalse(userAddress.isPresent());
//        Optional<Event> eventUpdated1 = eventRepository.findById(event1.getId());
//        Assert.assertTrue(eventUpdated1.isPresent());
//        Assert.assertTrue(eventUpdated1.get().isCostumerNull());
//        Optional<Event> eventUpdated2 = eventRepository.findById(event2.getId());
//        Assert.assertTrue(eventUpdated2.isPresent());
//        Assert.assertTrue(eventUpdated2.get().isCostumerNull());
//        Optional<Customer> optionalCostumer = customerRepository.findById(event1.getCostumer().getId());
//        Assert.assertFalse(optionalCostumer.isPresent());
//        Optional<Customer> optionalCostumer2 = customerRepository.findById(event2.getCostumer().getId());
//        Assert.assertFalse(optionalCostumer2.isPresent());
//
//    }

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

    private CompanyForm initCompanyForm(Address address) {
        CompanyForm companyForm = new CompanyForm();
        companyForm.setAddress(address);
        companyForm.setName("Test Company 2");
        companyForm.setRn("Test Registered Number 2");
        List<OpeningHoursForm> openingHoursForm = new ArrayList<>();
        for (int day = 2; day <= 5; day++) {
            openingHoursForm.add(new OpeningHoursForm(LocalTime.of(8, 0), LocalTime.of(12, 0), day));
            openingHoursForm.add(new OpeningHoursForm(LocalTime.of(13, 30), LocalTime.of(19, 0), day));
        }
        openingHoursForm.add(new OpeningHoursForm(LocalTime.of(8, 0), LocalTime.of(17, 0), 6));
        companyForm.setOpeningHours(openingHoursForm);
        return companyForm;
    }

    private UserForm initUser(Address address) {
        UserForm userForm = new UserForm();
        userForm.setName("Test Name 2");
        userForm.setAddress(address);
        userForm.setPassword(passwordEncoder());
        userForm.setEmail("professional2@email.com");
        userForm.setPhone("Test Phone 2");
        return userForm;
    }

    private String passwordEncoder() {
        return new BCryptPasswordEncoder().encode("654321");
    }
}
