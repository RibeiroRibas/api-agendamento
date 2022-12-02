package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profiles,Long> {

    Profiles findByProfileName(String nameProfile);
}
