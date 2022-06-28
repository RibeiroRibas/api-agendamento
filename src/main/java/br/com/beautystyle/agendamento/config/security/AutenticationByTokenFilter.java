package br.com.beautystyle.agendamento.config.security;

import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticationByTokenFilter extends OncePerRequestFilter {

    // não é possivel injetar dependencias pois essa classe não é um bean do Spring
    private final TokenServices tokenServices;

    private final UserRepository repository;

    //aqui usei um construtor pois não é possivel injetar dependencia dentro dessa classe
    public AutenticationByTokenFilter(TokenServices tokenServices,UserRepository repository) {
        this.tokenServices = tokenServices;
        this.repository = repository;
    }

    // metodo para verificar se o usuario tem um acesso liberado via token,
    // o client não está logado na api sendo assim para cada requisição é preciso validar o token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getToken(request); // metodo que recupera o token do client que solicitou a requisição
        boolean valido = tokenServices.isValidateToken(token);// valida o token
        if(valido){
            autenticarCliente(token);
        }
        filterChain.doFilter(request,response);
    }

    // ultimo passo para autenticar o usuario via token
    private void autenticarCliente(String token) {
        // dentro do token tem o id do usuario
        Long userId = tokenServices.getUserId(token);
        // recuperei o usuario pelo id
        User user = repository.findById(userId).get();
        // cria o autenticaor, passando usuario, senha é null pq não precisa,
        // o terceiro parametro é o perfil do usuario
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        //classe do spring que força a autenticação
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7, token.length());
    }
}
