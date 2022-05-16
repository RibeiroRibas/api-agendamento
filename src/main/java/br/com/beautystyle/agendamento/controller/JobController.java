package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.form.JobForm;
import br.com.beautystyle.agendamento.model.Job;
import br.com.beautystyle.agendamento.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    @Cacheable(value = "jobList")
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<Job> insert(@RequestBody @Valid JobForm jobForm, UriComponentsBuilder uriBuilder) {
        Job savedJob = jobRepository.save(jobForm.convert());
        URI uri = uriBuilder.path("/job/{id}")
                .buildAndExpand(savedJob.getJobId())
                .toUri();
        return ResponseEntity.created(uri).body(savedJob);
    }

    @PutMapping
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<Job> update(@RequestBody @Valid Job job){
        Optional<Job> jobOptional = jobRepository.findById(job.getJobId());
        if(jobOptional.isPresent()){
            Job jobUpdated = job.update(jobRepository);
            return ResponseEntity.ok(jobUpdated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            jobRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
