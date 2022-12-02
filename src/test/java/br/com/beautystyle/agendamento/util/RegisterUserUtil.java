package br.com.beautystyle.agendamento.util;

import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.LoginForm;
import br.com.beautystyle.agendamento.model.CompanyType;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.repository.ProfileRepository;
import br.com.beautystyle.agendamento.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.beautystyle.agendamento.controller.ConstantsController.USER_PROFILE_C;
import static br.com.beautystyle.agendamento.controller.ConstantsController.USER_PROFILE_P;
import static br.com.beautystyle.agendamento.util.ConstantsTest.*;

@Service
public class RegisterUserUtil {

    private ObjectMapper mapper;
    private MockMvc mockMvc;
    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    public RegisterUserUtil() {
    }

    public RegisterUserUtil(ProfileRepository profileRepository,
                            UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.userRepository.deleteAll();
        this.profileRepository.deleteAll();
    }

    public RegisterUserUtil(ProfileRepository profileRepository,
                            UserRepository userRepository,
                            ObjectMapper mapper,
                            MockMvc mockMvc) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.mockMvc = mockMvc;
        this.userRepository.deleteAll();
        this.profileRepository.deleteAll();
    }

    public UserCustomer saveUserCostumer() {
        UserCustomer user = initUserCostumer();
        return userRepository.save(user);
    }

//    public UserCustomerTest saveUserCostumerTest() {
//        UserCustomerTest user = initUserCostumerTest();
//        return userRepository.save(user);
//    }

    private UserCustomer initUserCostumer() {
        Profiles savedProfile = saveProfile(USER_PROFILE_C);
        Address address = initAddress();
        UserCustomer user = new UserCustomer();
        user.setName("Ribeiro");
        user.setAddress(address);
        user.setPassword(passwordEncoder());
        user.setEmail(CUSTOMER_EMAIL);
        user.setPhone("Test Phone");
        user.setProfiles(savedProfile);
        user.setCustomer(new Customer("Rafael", "Test Phone",INVALID_TENANT));
        return user;
    }

//    private UserCustomerTest initUserCostumerTest() {
//        Profiles savedProfile = saveProfile(USER_PROFILE_C);
//        Address address = initAddress();
//        UserCustomerTest user = new UserCustomerTest();
//        user.setName("Ribeiro");
//        user.setAddress(address);
//        user.setPassword(passwordEncoder());
//        user.setEmail(CUSTOMER_EMAIL);
//        user.setPhone("Test Phone");
//        user.setProfiles(savedProfile);
//        user.setCustomer(new CustomerTest("Rafael", "Test Phone", INVALID_TENANT));
//        return user;
//    }

    private String passwordEncoder() {
        return new BCryptPasswordEncoder().encode(USER_PASSWORD);
    }

    public Address initAddress() {
        Address address = new Address();
        address.setCity("Test City");
        address.setCountry("Test Country");
        address.setNumber(10);
        address.setState("Test State");
        address.setZipCode("Test Cip Code");
        address.setStreet("Test Street");
        return address;
    }

    public UserProfessional saveUserProfessional() {
        UserProfessional user = initUserProfessional();
        return userRepository.save(user);
    }

    private UserProfessional initUserProfessional() {
        Profiles savedProfile = saveProfile(USER_PROFILE_P);
        Company company = initCompany();
        UserProfessional user = new UserProfessional();
        user.setName("Test Name");
        user.setAddress(initAddress());
        user.setPassword(passwordEncoder());
        user.setEmail(PROFESSIONAL_EMAIL);
        user.setPhone("Test Phone");
        user.setProfiles(savedProfile);
        user.setCompany(company);
        return user;
    }

    private Company initCompany() {
        Company company = new Company();
        company.setAddress(initAddress());
        company.setName("Test Company");
        company.setRn("Test Registered Number");
        company.setType(CompanyType.SALON);
        List<OpeningHours> openingHours = new ArrayList<>();
        for (int day = 2; day <= 5; day++) {
            openingHours.add(new OpeningHours(LocalTime.of(8, 0), LocalTime.of(12, 0), day));
            openingHours.add(new OpeningHours(LocalTime.of(13, 30), LocalTime.of(19, 0), day));
        }
        openingHours.add(new OpeningHours(LocalTime.of(8, 0), LocalTime.of(17, 0), 6));
        BusinessHours businessHours = new BusinessHours();
        businessHours.setDescription("De Terça á Sexta das 8:00 ás 12:00 e das 13:30 ás 19:00. Sábado das 8:00 ás 17:00");
        businessHours.setOpeningHours(openingHours.stream().sorted().collect(Collectors.toList()));
        businessHours.setTimeInterval(30);
        company.setBusinessHours(businessHours);
        return company;
    }

    public Profiles saveProfile(String userProfileP) {
        Profiles profile = new Profiles();
        profile.setProfileName(userProfileP);
        return profileRepository.save(profile);
    }

    public TokenDto authUserProfessional() throws Exception {
        LoginForm formProfessional = new LoginForm(PROFESSIONAL_EMAIL, USER_PASSWORD);
        return whenUserIsAuthenticateThenReturnTokenDto(formProfessional);
    }

    public TokenDto whenUserIsAuthenticateThenReturnTokenDto(LoginForm form) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(new URI("/auth"))
                        .content(mapper.writeValueAsString(form))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return mapper.readValue(contentAsString, TokenDto.class);
    }

    public String authUserCostumer() throws Exception {
        LoginForm formCostumer = new LoginForm(CUSTOMER_EMAIL, USER_PASSWORD);
        TokenDto tokenDto = whenUserIsAuthenticateThenReturnTokenDto(formCostumer);
        return tokenDto.getToken();
    }


}
