package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenServices tokenServices;
    @Autowired
    private HttpServletRequest request;

    public User findById(){
        Long id = tokenServices.getUserId(request);
        return userRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(ENTITY_NOT_FOUND)
        );
    }

}
