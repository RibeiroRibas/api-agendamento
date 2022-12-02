package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.controller.form.EventAvailableTimesForm;
import br.com.beautystyle.agendamento.model.entity.Job;
import br.com.beautystyle.agendamento.model.entity.Schedule;
import br.com.beautystyle.agendamento.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Set<Job> findAllById(Iterable<Long> ids){
        return new HashSet<>(jobRepository.findAllById(ids));
    }

    public void removeAssociation(Schedule schedule) {
        schedule.getJobs().forEach(job -> {
            Job jobById = jobRepository.getById(job.getId());
            jobById.removeAssociation(schedule);
        });
    }

}
