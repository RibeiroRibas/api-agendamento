package br.com.beautystyle.agendamento.controller.dto;

import br.com.beautystyle.agendamento.model.entity.Address;
import br.com.beautystyle.agendamento.model.entity.Profiles;
import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.repository.UserRepository;

public class UserDto {

    private Long apiId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Address address;
    private String profiles;

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public UserDto() {
    }

    public UserDto(User savedUser) {
        this.apiId = savedUser.getId();
        this.phone = savedUser.getPhone();
        this.name = savedUser.getName();
        this.address = savedUser.getAddress();
        this.profiles = savedUser.getProfiles().get(0).getNameProfile();
        this.email = savedUser.getEmail();;
        this.password = savedUser.getPassword();
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

    public String getProfile() {
        return profiles;
    }

    public void setProfile(String profiles) {
        this.profiles = profiles;
    }

    public User converter() {
        User user = new User();
        user.setPassword(this.password);
        user.setPhone(this.phone);
        user.setName(this.name);
        user.setEmail(this.email);
        user.setProfiles(new Profiles(this.profiles));
        user.setAddress(this.address);
        return user;
    }

    public User update(UserRepository userRepository) {
        User user = userRepository.getById(this.apiId);
        user.setAddress(this.address);
        user.setProfiles(new Profiles(this.profiles));
        user.setPhone(this.phone);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }
}
