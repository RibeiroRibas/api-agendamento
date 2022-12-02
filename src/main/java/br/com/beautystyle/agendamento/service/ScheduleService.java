package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.exceptions.EntityNotFoundException;
import br.com.beautystyle.agendamento.model.entity.Customer;
import br.com.beautystyle.agendamento.model.entity.Schedule;
import br.com.beautystyle.agendamento.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static br.com.beautystyle.agendamento.controller.ConstantsController.ENTITY_NOT_FOUND;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND)
        );
    }

    public List<Schedule> findByCustomerUserCustomerId(Long id) {
        return scheduleRepository.findByCustomerUserCustomerId(id);
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    public Schedule updateByCustomer(Schedule schedule) {
        Schedule scheduleToUpdate = scheduleRepository.getById(schedule.getId());
        scheduleToUpdate.update(schedule);
        return scheduleToUpdate;
    }

    public List<Schedule> findByDateAndTenant(LocalDate date, Long tenant) {
        return scheduleRepository.findByDateAndTenant(date, tenant);
    }

    public void updateByProfessional(Schedule schedule) {
        Schedule scheduleToUpdate = scheduleRepository.getById(schedule.getId());
        scheduleToUpdate.updateByProfessional(schedule);
    }

    public void removeAssociation(Customer customer) {
        List<Schedule> schedules = scheduleRepository.findByCustomerId(customer.getId());
        schedules.forEach(schedule -> {
            Schedule scheduleToUpdate = scheduleRepository.getById(schedule.getId());
            scheduleToUpdate.setCustomer(null);
        });
    }
}
