package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.JobDto;
import br.com.beautystyle.agendamento.model.entity.Job;
import br.com.beautystyle.agendamento.repository.EventRepository;
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
import java.util.Set;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/{id}")
    @Cacheable(value = "jobList")
    public Set<JobDto> findByCompanyId(@PathVariable Long id) {
        Set<Job> jobList = jobRepository.findByCompanyId(id);
        return JobDto.convert(jobList);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<JobDto> insert(@RequestBody @Valid JobDto jobDto, UriComponentsBuilder uriBuilder) {
        Job savedJob = jobRepository.save(jobDto.convert());
        URI uri = uriBuilder.path("/job/{id}")
                .buildAndExpand(savedJob.getJobId())
                .toUri();
        return ResponseEntity.created(uri).body(new JobDto(savedJob));
    }

    @PutMapping
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<?> update(@RequestBody @Valid JobDto jobDto){
        Optional<Job> jobOptional = jobRepository.findById(jobDto.getApiId());
        if(jobOptional.isPresent()){
            jobDto.update(jobRepository);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "jobList", allEntries = true)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            jobOptional.get().removeAssociation(jobRepository,eventRepository);
            jobRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
