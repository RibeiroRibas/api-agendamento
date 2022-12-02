package br.com.beautystyle.agendamento.controller.form;

import br.com.beautystyle.agendamento.model.entity.Job;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

public class JobEventForm {

    @NotNull
    private Long apiId;

    public JobEventForm() {
    }

    public static Set<JobEventForm> convert(Set<Job> jobs) {
        return jobs.stream().map(JobEventForm::new).collect(Collectors.toSet());
    }

    public JobEventForm(Job job) {
        this.apiId = job.getId();
    }


    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }
}
