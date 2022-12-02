package br.com.beautystyle.agendamento.util;

import br.com.beautystyle.agendamento.model.entity.Job;

import java.time.LocalTime;
import java.util.Set;

public class JobUtil {

    public static LocalTime sumJobsDurationTime(Set<Job> foundJobs) {
        LocalTime durationTime = LocalTime.of(0, 0);
        for (Job job : foundJobs) {
            durationTime = job.sumJobsDurationTime(durationTime);
        }
        return durationTime;
    }

}
