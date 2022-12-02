package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.exceptions.EntityNotFoundException;
import br.com.beautystyle.agendamento.model.entity.BlockWeekDayTime;
import br.com.beautystyle.agendamento.repository.BlockWeekDayTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;

@Service
public class BlockWeekDayTimeService {

    @Autowired
    private BlockWeekDayTimeRepository repository;


    public BlockWeekDayTime findById(Long id) {
        return repository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(ENTITY_NOT_FOUND)
        );
    }
}
