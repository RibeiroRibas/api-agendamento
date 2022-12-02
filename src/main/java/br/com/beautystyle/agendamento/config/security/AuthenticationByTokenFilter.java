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
import java.util.Optional;

public class AuthenticationByTokenFilter extends OncePerRequestFilter {

    private final TokenServices tokenServices;

    private final UserRepository repository;

    public AuthenticationByTokenFilter(TokenServices tokenServices, UserRepository repository) {
        this.tokenServices = tokenServices;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getToken(request);
        if (tokenServices.isValidateToken(token))
            authClient(token);
        filterChain.doFilter(request, response);
    }

    private void authClient(String token) {

        Long userId = tokenServices.getUserId(token);
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.get().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }
}
