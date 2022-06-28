package br.com.beautystyle.agendamento.model.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Profiles implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nameProfile; // cliente, profisional

    public Profiles(String nameProfile) {
        if(nameProfile.equals("ROLE_PROFISSIONAL")){
            this.id = 1L;

        }else{
            this.id = 2L;
        }
        this.nameProfile=nameProfile;
    }

    public Profiles() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProfile() {
        return nameProfile;
    }

    public void setNameProfile(String nameProfile) {
        this.nameProfile = nameProfile;
    }

    @Override
    public String getAuthority() {
        return this.nameProfile;
    }
}
