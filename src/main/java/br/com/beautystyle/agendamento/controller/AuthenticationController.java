package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.TokenCostumerDto;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.LoginForm;
import br.com.beautystyle.agendamento.model.ResponseHandler;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.beautystyle.agendamento.controller.ConstantsController.BAD_CREDENTIALS;

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod", "test"})
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenServices tokenService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginForm loginForm) {
        UsernamePasswordAuthenticationToken loginData = loginForm.converter();
        try {
            Authentication authenticate = authManager.authenticate(loginData);
            String token = tokenService.createToken(authenticate);
            //-------------------------------
            Long userId = tokenService.getUserId(token);
            Optional<User> userById = userRepository.findById(userId);
            if (userById.isPresent()) {
                User user = userById.get();
                List<String> profiles = user.getProfiles()
                        .stream()
                        .map(Profiles::getProfileName)
                        .collect(Collectors.toList());
                if (user instanceof UserProfessional) {
                    UserProfessional userProfessional = (UserProfessional) user;
                    Long tenant = userProfessional.getTenant();
                    Optional<Company> optionalCompany = companyRepository.findById(tenant);
                    if (optionalCompany.isPresent())
                        return ResponseEntity.ok(new TokenDto(token, "Bearer", tenant, profiles,
                                optionalCompany.get().getBusinessHours()));
                }
                if (user instanceof UserCustomer) {
                    return ResponseEntity.ok(new TokenCostumerDto(token, "Bearer"));
                }
            }
            return ResponseEntity.notFound().build();
        } catch (AuthenticationException e) {
            return ResponseHandler.generateResponse(BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
        }
    }
}
