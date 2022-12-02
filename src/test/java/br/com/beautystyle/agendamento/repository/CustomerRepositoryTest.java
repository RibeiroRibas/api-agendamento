package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Customer;
import br.com.beautystyle.agendamento.util.RegisterScheduleUtil;
import br.com.beautystyle.agendamento.util.RegisterUserUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static br.com.beautystyle.agendamento.util.ConstantsTest.INVALID_TENANT;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private JobRepository jobRepository;
    private RegisterScheduleUtil registerScheduleUtil;
    private Long tenant;

    @Before
    public void setup() {
        registerScheduleUtil = new RegisterScheduleUtil(scheduleRepository, jobRepository, customerRepository);
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository);
        tenant = registerUserUtil.saveUserProfessional().getCompany().getId();
    }

    @Test
    public void shouldReturnCostumerListByTenant() {
        registerScheduleUtil.saveCostumerTest(tenant);
        registerScheduleUtil.saveCostumerTest(INVALID_TENANT);
        List<Customer> customers = customerRepository.findByTenant(tenant);
        Assert.assertEquals(1, customers.size());
    }

}
