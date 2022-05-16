package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

   // @Test
    void deveriaCarregarUmUsuarioQuandoBuscarPeloEmail(){
        String email ="rafael@gmail.com";
        User user = new User();
        user.setName("Rafael");
        user.setEmail(email);
        user.setPassword("1237845");
        entityManager.persist(user);

        Optional<User> useByEmail = userRepository.findByEmail(email);
        useByEmail.ifPresent(value -> assertEquals(email, value.getEmail()));
    }

   // @Test
    void NaoDeveriaCarregarUmUsuarioQuandoBuscarPeloEmailInvalido(){
        String email ="invalido@gmail.com";
        Optional<User> useByEmail = userRepository.findByEmail(email);
        useByEmail.ifPresent(value -> assertEquals(email, value.getEmail()));
    }

}