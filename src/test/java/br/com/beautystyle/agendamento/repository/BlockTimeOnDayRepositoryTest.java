package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.util.RegisterBlockTimeUtil;
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

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BlockTimeOnDayRepositoryTest {

    @Autowired
    private BlockTimeOnDayRepository blockTimeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    private Long tenant;

    @Before
    public void setup() {
        blockTimeRepository.deleteAll();
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository);
        tenant = registerUserUtil.saveUserProfessional().getCompany().getId();
    }

    @Test
    public void shouldReturnBlockTimeByDateAndTenant() {
        RegisterBlockTimeUtil registerBlockTimeUtil = new RegisterBlockTimeUtil(blockTimeRepository);
        registerBlockTimeUtil.saveAllBlockTimesOnDay(tenant);
        Assert.assertEquals(2, blockTimeRepository.findByDateAndTenant(
                registerBlockTimeUtil.getEventDateEqualsWednesday(), tenant).size());
    }
}
