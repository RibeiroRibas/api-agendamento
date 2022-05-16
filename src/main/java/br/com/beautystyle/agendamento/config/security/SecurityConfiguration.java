package br.com.beautystyle.agendamento.config.security;

import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Profile({"prod"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String USERPROFILE_P = "PROFESSIONAL";
    public static final String USERPROFILE_M = "MODERATOR";

    @Autowired
    private TokenServices tokenServices;

    @Autowired
    private UserRepository repository;

    @Autowired
    private AutenticationService autenticationService;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //configurações de autenticação
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticationService).passwordEncoder(new BCryptPasswordEncoder());// transformar a senha em hash por questao de securança
    }

    //configurações de autorização
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        clientAuthorizeRequests(http);
        serviceAuthorizeRequests(http);
        expenseAuthorizeServices(http);
        eventAuthorizeServices(http);
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticationByTokenFilter(tokenServices, repository),
                        UsernamePasswordAuthenticationFilter.class);
    }

    //Static resources configuration (css, js, img, etc.)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**.html",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/configuration/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**");
    }


    private void clientAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/client").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.GET, "/client/*").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.POST, "/client").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.PUT, "/client").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.DELETE, "/client").hasAnyRole(USERPROFILE_P, USERPROFILE_M);
    }

    private void serviceAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/service").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.POST, "/service").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.PUT, "/service").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.DELETE, "/service").hasAnyRole(USERPROFILE_P, USERPROFILE_M);
    }

    private void expenseAuthorizeServices(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/expense").hasRole(USERPROFILE_P)
                .antMatchers(HttpMethod.GET, "/expense").hasRole(USERPROFILE_P)
                .antMatchers(HttpMethod.PUT, "/expense").hasRole(USERPROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/expense").hasRole(USERPROFILE_P);
    }

    private void eventAuthorizeServices(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/event").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.GET, "/event/*").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.POST, "/event").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.PUT, "/event").hasAnyRole(USERPROFILE_P, USERPROFILE_M)
                .antMatchers(HttpMethod.DELETE, "/event").hasAnyRole(USERPROFILE_P, USERPROFILE_M);
    }

}
