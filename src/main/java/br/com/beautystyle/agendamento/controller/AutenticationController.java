package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.CompanyService;
import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.LoginForm;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.model.entity.Profiles;
import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Profile({"dev","prod"})
public class AutenticationController {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenServices tokenService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm user) {
        UsernamePasswordAuthenticationToken dadosLogin = user.converter();
        try {
            Authentication authenticate = authManager.authenticate(dadosLogin); // se estiver tudo ok com o login e senha
            String token = tokenService.createToken(authenticate);
           //-------------------------------
            Long userId = tokenService.getUserId(token);
            Optional<User> userById = userRepository.findById(userId);
            List<Profiles> profileList = new ArrayList<>();
            if (userById.isPresent()) {
                profileList = userById.get().getProfiles();
            }
            List<String> profiles = profileList.stream().map(Profiles::getNameProfile).collect(Collectors.toList());
            Company company = companyRepository.findByUserId(userId);
           //------------------------------
            return ResponseEntity.ok(new TokenDto(token, "Bearer",company.getId(),profiles));
        } catch (AuthenticationException e) { // se não devolve uma exceção
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
