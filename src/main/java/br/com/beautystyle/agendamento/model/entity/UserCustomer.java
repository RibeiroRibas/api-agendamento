package br.com.beautystyle.agendamento.model.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("user_customer")
public class UserCustomer extends User {

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isUserIdNotEquals(Long userId) {
        return !getId().equals(userId);
    }
}
