package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.exceptions.EntityNotFoundException;
import br.com.beautystyle.agendamento.controller.form.BlockTimeOnDayForm;
import br.com.beautystyle.agendamento.model.ResponseHandler;
import br.com.beautystyle.agendamento.model.entity.BlockTimeOnDay;
import br.com.beautystyle.agendamento.repository.BlockTimeOnDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;
import static br.com.beautystyle.agendamento.controller.ConstantsController.REQUEST_OK;

@Service
public class BlockTimeOnDayService {

    @Autowired
    private BlockTimeOnDayRepository repository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TokenServices tokenServices;
    @Autowired
    private EventTimeByProfessionalService eventTimeService;

    public BlockTimeOnDay findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND)
        );
    }

    public ResponseEntity<?> insert(BlockTimeOnDayForm blockTimeForm) {

        Long tenant = tokenServices.getTenant(request);

        BlockTimeOnDay blockTimeOnDay = new BlockTimeOnDay(blockTimeForm, tenant);

        ResponseEntity<?> responseEntityStatusConflict = eventTimeService.checkEventTimeIsAvailable(
                blockTimeOnDay, blockTimeOnDay.getDate());

        if (responseEntityStatusConflict != null)
            return responseEntityStatusConflict;

        return responseEntityStatusCreated(repository.save(blockTimeOnDay));
    }



    private ResponseEntity<?> responseEntityStatusCreated(BlockTimeOnDay blockTimeOnDay) {
        return ResponseHandler.generateResponseWithData(
                REQUEST_OK,
                HttpStatus.CREATED,
                blockTimeOnDay);
    }

    public ResponseEntity<?> update(BlockTimeOnDayForm blockTimeForm, Long id) {

        findById(id);

        Long tenant = tokenServices.getTenant(request);

        BlockTimeOnDay blockTimeOnDay = new BlockTimeOnDay(blockTimeForm, tenant);
        blockTimeOnDay.setId(id);

        ResponseEntity<?> responseEntityStatusConflict = eventTimeService.checkEventTimeIsAvailable(
                blockTimeOnDay, blockTimeOnDay.getDate());

        if (responseEntityStatusConflict != null)
            return responseEntityStatusConflict;

        BlockTimeOnDay blockTimeToUpdate = repository.getById(id);
        blockTimeToUpdate.update(blockTimeOnDay);

        return ResponseEntity.ok().build();
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
