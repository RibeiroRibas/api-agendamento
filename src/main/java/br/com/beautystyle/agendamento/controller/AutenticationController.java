package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.TokenDto;
import br.com.beautystyle.agendamento.controller.form.LoginForm;
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

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod"})
public class AutenticationController {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenServices tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm user){
        UsernamePasswordAuthenticationToken dadosLogin = user.converter();
        try {
            Authentication authenticate = authManager.authenticate(dadosLogin); // se tiver tudo ok com o login e senha
            String token = tokenService.createToken(authenticate);
            return ResponseEntity.ok(new TokenDto(token,"Bearer"));
        }catch (AuthenticationException e){ // se não devolve uma exceção
            return ResponseEntity.badRequest().build();
        }
    }
}
