package br.com.beautystyle.agendamento.model.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("user_professional")
public class UserProfessional extends User{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getTenant(){
        return company.getId();
    }
}
