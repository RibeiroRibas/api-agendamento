package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.BlockWeekDayTime;
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

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BlockWeekDayTimeRepositoryTest {

    @Autowired
    private BlockWeekDayTimeRepository blockTimeRepository;
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
        registerBlockTimeUtil.saveAllBlockWeekDayTimes(tenant);
        List<BlockWeekDayTime> blockTimes = blockTimeRepository.findByTenantEqualsAndDayOfWeekEqualsAndEndDateGreaterThanEqual(tenant, '3',
                registerBlockTimeUtil.getEventDateEqualsWednesday());
        blockTimes = blockTimes.stream()
                .filter(blockTimeEveryDay -> blockTimeEveryDay
                        .getExceptionDates()
                        .stream()
                        .noneMatch(exceptionDate -> exceptionDate.equals(registerBlockTimeUtil.getEventDateEqualsWednesday())))
                .collect(Collectors.toList());
        Assert.assertEquals(1,
                blockTimes.size());
    }
}
