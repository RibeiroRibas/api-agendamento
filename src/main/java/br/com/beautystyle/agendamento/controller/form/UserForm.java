package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UserForm {

    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String phone;
    @NotNull
    @Valid
    private Address address;

    public UserForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private String passwordEncoder() {
        return new BCryptPasswordEncoder().encode(password);
    }

    public UserCustomer convertCostumer(Profiles profile) {
        UserCustomer user = new UserCustomer();
        user.setCustomer(new Customer(name,phone,-1L));
        setUser(user,profile);
        return user;
    }
    private void setUser(User user,Profiles profile){
        user.setPhone(phone);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder());
        user.setAddress(address);
        user.setProfiles(profile);
    }

    public UserProfessional convertProfessional(Profiles profile) {
        UserProfessional user = new UserProfessional();
        setUser(user,profile);
        return user;
    }

    public void update(Long id, UserRepository userRepository) {
        User user = userRepository.getById(id);
        user.setAddress(address);
        user.setName(name);
        user.setPassword(password);
        user.setPhone(phone);
        user.setEmail(email);
    }

}
