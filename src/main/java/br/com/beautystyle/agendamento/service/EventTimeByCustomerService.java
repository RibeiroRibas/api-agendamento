package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.exceptions.EventDateNotAvailableException;
import br.com.beautystyle.agendamento.controller.exceptions.EventTimeNotAvailableException;
import br.com.beautystyle.agendamento.controller.exceptions.RequestNotAllowException;
import br.com.beautystyle.agendamento.controller.form.EventAvailableTimesForm;
import br.com.beautystyle.agendamento.model.EventAvailableTimes;
import br.com.beautystyle.agendamento.model.entity.Company;
import br.com.beautystyle.agendamento.model.entity.EventTime;
import br.com.beautystyle.agendamento.model.entity.OpeningHours;
import br.com.beautystyle.agendamento.model.entity.Schedule;
import br.com.beautystyle.agendamento.util.EventTimeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;

@Service
public class EventTimeByCustomerService {


    @Autowired
    private EventTimeComponent eventTimeComponent;
    @Autowired
    private CompanyService companyService;

    public void checkEventTimeIsAvailable(Schedule schedule) {

        List<EventTime> eventTimes = eventTimeComponent.getEventTimes(schedule.getDate(), schedule.getTenant());

        if (schedule.isEventTimeNotAvailable(eventTimes))
            throw new EventTimeNotAvailableException(EVENT_TIME_IS_NOT_AVAILABLE);
    }

    public void checkEventDateIsAvailable(Schedule schedule, Company company) {

        List<OpeningHours> openingHours = company.getBusinessHours().getOpeningHours();

        if (schedule.isEventDateNotAvailable(openingHours))
            throw new EventDateNotAvailableException(EVENT_DATE_NOT_AVAILABLE);

    }

    public void checkEventStartingIn12hrsOrLess(Schedule schedule) {
        if (schedule.isEventStartingIn12hrsOrLess())
            throw new RequestNotAllowException(UPDATE_OR_DELETE_NOT_ALLOW);
    }

    public EventAvailableTimes getEventAvailableTimesObject(EventAvailableTimesForm eventForm, Long tenant) {

        Company company = companyService.findById(tenant);

        checkEventDateIsAvailable(new Schedule(eventForm.getEventDate()), company);

        List<EventTime> eventTimes = eventTimeComponent.getEventTimes(eventForm.getEventDate(), tenant);

        return new EventAvailableTimes(eventTimes, company.getBusinessHours());
    }

}
