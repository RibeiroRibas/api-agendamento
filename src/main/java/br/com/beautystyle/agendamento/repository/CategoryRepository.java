package br.com.beautystyle.agendamento.repository;

import br.com.beautystyle.agendamento.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findByTenant(Long id);
}
