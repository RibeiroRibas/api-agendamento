package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.JobDto;
import br.com.beautystyle.agendamento.controller.form.JobForm;
import br.com.beautystyle.agendamento.model.entity.Job;
import br.com.beautystyle.agendamento.repository.JobRepository;
import br.com.beautystyle.agendamento.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

import static br.com.beautystyle.agendamento.controller.ConstantsController.JOB_NOT_FOUND;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TokenServices tokenServices;

    @GetMapping
    @Cacheable(value = "jobList")
    public ResponseEntity<Set<JobDto>> findByTenant(HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        Set<Job> jobList = jobRepository.findByTenant(tenant);
        return ResponseEntity.ok(JobDto.convertList(jobList));
    }

//    @GetMapping("/available/{tenant}")
//    public ResponseEntity<?> availableJobs(@PathVariable Long tenant) {
//        Optional<Company> company = companyRepository.findById(tenant);
//        if (!company.isPresent())
//            throw new EntityNotFoundException(COMPANY_NOT_FOUND);
//        Set<Job> jobs = jobRepository.findByTenant(tenant);
//        return ResponseEntity.ok(JobByCostumerDto.convert(jobs));
//    }

    @PostMapping
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<JobDto> insert(@RequestBody @Valid JobForm jobForm,
                                         UriComponentsBuilder uriBuilder,
                                         HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        jobForm.setTenant(tenant);
        Job savedJob = jobRepository.save(new Job(jobForm));
        URI uri = uriBuilder.path("/job/{id}")
                .buildAndExpand(savedJob.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new JobDto(savedJob));
    }


    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody @Valid JobForm jobForm,
                                    HttpServletRequest request) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job updatedJob = jobForm.update(id, jobRepository);
            return ResponseEntity.ok(new JobDto(updatedJob));
        }
        throw new EntityNotFoundException(JOB_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpServletRequest request) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
          //  jobOptional.get().removeAssociation(jobRepository, scheduleRepository);
            jobRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException(JOB_NOT_FOUND);
    }
}
