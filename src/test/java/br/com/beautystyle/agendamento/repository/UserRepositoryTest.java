package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.util.RegisterUserUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void givenUserEmailWhenFindByEmailThenReturnUserObject() {
    //    eventRepository.deleteAll();
        RegisterUserUtil registerUserUtil = new RegisterUserUtil(profileRepository,
                userRepository);
        User savedUser = registerUserUtil.saveUserCostumer();
        String email = savedUser.getEmail();
        Optional<User> user = userRepository.findByEmail(email);
        Assert.assertTrue(user.isPresent());
    }

}
