package br.com.beautystyle.agendamento.model.entity;

import br.com.beautystyle.agendamento.model.CompanyType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String rn;
    @NotNull
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull
    private CompanyType type;
    @OneToOne(mappedBy = "company")
    private UserProfessional professional;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @NotNull
    private Address address;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_hours_id", referencedColumnName = "id")
    @NotNull
    private BusinessHours businessHours;


    public BusinessHours getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(BusinessHours businessHours) {
        this.businessHours = businessHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfessional getProfessional() {
        return professional;
    }

    public void setProfessional(UserProfessional professional) {
        this.professional = professional;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CompanyType getType() {
        return type;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }
}
