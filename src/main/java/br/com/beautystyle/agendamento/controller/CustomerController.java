package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.CustomerDto;
import br.com.beautystyle.agendamento.controller.exceptions.TenantNotEqualsException;
import br.com.beautystyle.agendamento.controller.form.CustomerForm;
import br.com.beautystyle.agendamento.model.entity.Customer;
import br.com.beautystyle.agendamento.repository.CustomerRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.CUSTOMER_NOT_FOUND;
import static br.com.beautystyle.agendamento.controller.ConstantsController.TENANT_NOT_EQUALS;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TokenServices tokenServices;

    @GetMapping
    public List<CustomerDto> findAllByTenant(HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        List<Customer> customers = customerRepository.findByTenant(tenant);
        return CustomerDto.convert(customers);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CustomerDto> insert(@RequestBody @Valid CustomerForm customerForm,
                                              HttpServletRequest request,
                                              UriComponentsBuilder uriBuilder) {
        Long tenant = tokenServices.getTenant(request);
        customerForm.setTenant(tenant);
        Customer customer = customerRepository.save(new Customer(customerForm));
        URI uri = uriBuilder.path("/customer/{id}")
                .buildAndExpand(customer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new CustomerDto(customer));
    }

    @PostMapping("/insert_all")
    @Transactional
    public ResponseEntity<List<CustomerDto>> insertAll(@RequestBody @Valid List<CustomerForm> costumers,
                                                       HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        costumers.forEach(customerForm -> customerForm.setTenant(tenant));
        List<Customer> newCustomers = Customer.convert(costumers);
        List<Customer> savedCustomers = customerRepository.saveAll(newCustomers);
        return ResponseEntity.ok().body(CustomerDto.convert(savedCustomers));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody @Valid CustomerForm customerForm,
                                    HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        Optional<Customer> optionalCostumer = customerRepository.findById(id);
        if (optionalCostumer.isPresent()) {
            if (optionalCostumer.get().isTenantNotEquals(tenant))
                throw new TenantNotEqualsException(TENANT_NOT_EQUALS);
            Customer customer = customerForm.update(id, customerRepository);
            return ResponseEntity.ok(new CustomerDto(customer));
        }
        throw new EntityNotFoundException(CUSTOMER_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpServletRequest request) {
        Long tenant = tokenServices.getTenant(request);
        Optional<Customer> optionalCostumer = customerRepository.findById(id);
        if (optionalCostumer.isPresent()) {
            if (optionalCostumer.get().isTenantNotEquals(tenant))
                throw new TenantNotEqualsException(TENANT_NOT_EQUALS);
            Customer customer = customerRepository.getById(id);
            customer.removeEventAssociation(eventRepository);
            customerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        throw new EntityNotFoundException(CUSTOMER_NOT_FOUND);
    }
}
