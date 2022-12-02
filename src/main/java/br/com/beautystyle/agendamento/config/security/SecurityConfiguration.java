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
@Profile(value = {"prod", "test"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String USER_PROFILE_P = "PREMIUM_ACCOUNT";
    public static final String USER_PROFILE_C = "CUSTOMER";
    public static final String USER_PROFILE_A = "ADMIN";

    @Autowired
    private TokenServices tokenServices;

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        customerAuthorizeRequests(http);
        jobAuthorizeRequests(http);
        expenseAuthorizeRequests(http);
        categoryAuthorizeRequests(http);
        eventByProfessionalAuthorizeRequests(http);
        eventByCostumerAuthorizeRequests(http);
        companyAuthorizeRequests(http);
        userAuthorizeRequests(http);
        reportAuthorizeRequests(http);
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AuthenticationByTokenFilter(tokenServices, repository),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private void reportAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/report").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.GET, "/report/*").hasRole(USER_PROFILE_P);
    }

    private void eventByCostumerAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/event/by_customer/available_time").hasRole(USER_PROFILE_C)
                .antMatchers(HttpMethod.GET, "/event/by_customer").hasRole(USER_PROFILE_C)
                .antMatchers(HttpMethod.POST, "/event/by_customer").hasRole(USER_PROFILE_C)
                .antMatchers(HttpMethod.PUT, "/event/by_customer/*").hasRole(USER_PROFILE_C)
                .antMatchers(HttpMethod.DELETE, "/event/by_customer/*").hasAnyRole(USER_PROFILE_C);
    }

    private void companyAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/company/available").hasRole(USER_PROFILE_C)
                .antMatchers(HttpMethod.PUT, "/company").hasRole(USER_PROFILE_P);
    }

    private void userAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/user").hasAnyRole(USER_PROFILE_P, USER_PROFILE_C)
                .antMatchers(HttpMethod.POST, "/user/professional").permitAll()
                .antMatchers(HttpMethod.POST, "/user/customer").permitAll()
                .antMatchers(HttpMethod.DELETE, "/user/professional").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/user/customer").hasRole(USER_PROFILE_C);
    }

    private void categoryAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/category").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.POST, "/category").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/category/*").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/category/*").hasRole(USER_PROFILE_P);
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring()
                .antMatchers(
                        "/**.html",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/configuration/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**");
    }


    private void customerAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/customer").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.POST, "/customer").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.POST, "/customer/insert_all").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/customer").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/customer").hasRole(USER_PROFILE_P);
    }

    private void jobAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/job").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.GET, "/job/available/*").hasRole(USER_PROFILE_C)
                .antMatchers(HttpMethod.POST, "/job").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/job").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/job").hasRole(USER_PROFILE_P);
    }

    private void expenseAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/expense").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.GET, "/expense/*").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/expense").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/expense").hasRole(USER_PROFILE_P);
    }

    private void eventByProfessionalAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/event/by_professional/{eventDate}").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.POST, "/event/by_professional").hasAnyRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.PUT, "/event/by_professional/*").hasRole(USER_PROFILE_P)
                .antMatchers(HttpMethod.DELETE, "/event/by_professional/*").hasAnyRole(USER_PROFILE_P);
    }

}
