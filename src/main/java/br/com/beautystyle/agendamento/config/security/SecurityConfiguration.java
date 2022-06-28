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
@Profile(value = "prod")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String USER_PROFILE_P = "PROFISSIONAL";
    public static final String USER_PROFILE_C = "CLIENTE";

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
        jobAuthorizeRequests(http);
        expenseAuthorizeRequests(http);
        categoryAuthorizeRequests(http);
        eventAuthorizeRequests(http);
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AutenticationByTokenFilter(tokenServices, repository),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private void categoryAuthorizeRequests(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/category/*").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.POST, "/category").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/category").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/category").hasAnyRole(USER_PROFILE_P);
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
                .antMatchers(HttpMethod.GET, "/client/*").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.POST, "/client").hasAnyRole(USER_PROFILE_P, USER_PROFILE_C)
                .antMatchers(HttpMethod.POST, "/client/*").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/client").hasAnyRole(USER_PROFILE_P, USER_PROFILE_C)
                .antMatchers(HttpMethod.DELETE, "/client").hasAnyRole(USER_PROFILE_P, USER_PROFILE_C);
    }

    private void jobAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/job/*").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.POST, "/job").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/job").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/job").hasAnyRole(USER_PROFILE_P);
    }

    private void expenseAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/expense").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.GET, "/expense/*").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/expense").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/expense").hasRole(USER_PROFILE_P);
    }

    private void eventAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/event/*").permitAll()
                .antMatchers(HttpMethod.POST, "/event").hasAnyRole(USER_PROFILE_P, USER_PROFILE_C)
                .antMatchers(HttpMethod.PUT, "/event").hasAnyRole(USER_PROFILE_P, USER_PROFILE_C)
                .antMatchers(HttpMethod.DELETE, "/event").hasAnyRole(USER_PROFILE_P, USER_PROFILE_C);
    }

}
