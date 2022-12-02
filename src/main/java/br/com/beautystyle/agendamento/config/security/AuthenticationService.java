package br.com.beautystyle.agendamento.config.security;

import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // para fazer a consulta o spring precisa apenas no usuaria, a senha é feito internamente por baixo dos panos
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()){
            return user.get();
        }
        throw new UsernameNotFoundException("Dados inválido");
    }
}
