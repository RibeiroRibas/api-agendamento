package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.dto.ScheduleByCostumerDto;
import br.com.beautystyle.agendamento.controller.dto.EventTimeDto;
import br.com.beautystyle.agendamento.controller.form.EventAvailableTimesForm;
import br.com.beautystyle.agendamento.controller.form.ScheduleByCostumerForm;
import br.com.beautystyle.agendamento.model.EventAvailableTimes;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.util.JobUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleByCustomerService {

    @Autowired
    private UserService userService;
    @Autowired
    private JobService jobService;
    @Autowired
    private EventTimeByCustomerService eventTimeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ScheduleService scheduleService;

    public List<ScheduleByCostumerDto> findAll() {

        User user = userService.findById();

        List<Schedule> schedules = scheduleService.findByCustomerUserCustomerId(user.getId());

        Long tenant = getTenant(schedules);

        Company company = companyService.findById(tenant);

        return ScheduleByCostumerDto.convert(schedules, company);
    }

    private Long getTenant(List<Schedule> schedules) {
        return schedules.stream()
                .findFirst()
                .get()
                .getTenant();
    }

    public ScheduleByCostumerDto insert(ScheduleByCostumerForm eventForm) {

        Schedule schedule = initSchedule(eventForm);

        Company company = companyService.findById(schedule.getTenant());

        eventTimeService.checkEventTimeIsAvailable(schedule);

        eventTimeService.checkEventDateIsAvailable(schedule, company);

        Schedule savedSchedule = scheduleService.save(schedule);

        return new ScheduleByCostumerDto(savedSchedule, company);
    }

    private Schedule initSchedule(ScheduleByCostumerForm eventForm) {

        Set<Job> jobs = jobService.findAllById(eventForm.getJobIds());

        UserCustomer userCustomer = (UserCustomer) userService.findById();

        return new Schedule(eventForm, jobs, userCustomer.getCustomer());
    }

    public List<EventTimeDto> getAvailableTimes(EventAvailableTimesForm eventForm) {

        Set<Job> jobs = jobService.findAllById(eventForm.getJobIds());

        LocalTime durationTime = JobUtil.sumJobsDurationTime(jobs);

        Long tenant = jobs.stream().findFirst().get().getTenant();

        EventAvailableTimes eventAvailableTimes = eventTimeService.getEventAvailableTimesObject(eventForm, tenant);

        return eventAvailableTimes.getAvailableTimesDto(durationTime, eventForm.getEventDate());
    }

    public ScheduleByCostumerDto update(Long id, ScheduleByCostumerForm eventForm) {

        eventTimeService.checkEventStartingIn12hrsOrLess(scheduleService.findById(id));

        Set<Job> jobs = jobService.findAllById(eventForm.getJobIds());

        Schedule schedule = new Schedule(eventForm, jobs, id);

        Company company = companyService.findById(schedule.getTenant());

        eventTimeService.checkEventTimeIsAvailable(schedule);

        eventTimeService.checkEventDateIsAvailable(schedule, company);

        Schedule updatedSchedule = scheduleService.updateByCustomer(schedule);

        return new ScheduleByCostumerDto(updatedSchedule, company);
    }


    public void deleteById(Long id) {

        Schedule schedule = scheduleService.findById(id);

        eventTimeService.checkEventStartingIn12hrsOrLess(schedule);

        jobService.removeAssociation(schedule);

        scheduleService.deleteById(id);
    }

}
