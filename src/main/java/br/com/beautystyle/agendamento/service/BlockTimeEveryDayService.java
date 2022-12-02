package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.exceptions.EntityNotFoundException;
import br.com.beautystyle.agendamento.model.entity.BlockTimeEveryDay;
import br.com.beautystyle.agendamento.repository.BlockTimeEveryDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;

@Service
public class BlockTimeEveryDayService {

    @Autowired
    private BlockTimeEveryDayRepository repository;

    public BlockTimeEveryDay findById(Long id) {
        return repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(ENTITY_NOT_FOUND)
        );
    }
}
