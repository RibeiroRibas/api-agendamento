package br.com.beautystyle.agendamento.service;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.dto.CustomerDto;
import br.com.beautystyle.agendamento.controller.exceptions.EntityNotFoundException;
import br.com.beautystyle.agendamento.controller.exceptions.RequestNotAllowException;
import br.com.beautystyle.agendamento.controller.form.CustomerForm;
import br.com.beautystyle.agendamento.model.entity.Customer;
import br.com.beautystyle.agendamento.model.entity.Schedule;
import br.com.beautystyle.agendamento.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TokenServices tokenServices;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ScheduleService scheduleService;

    public void removeAssociation(Schedule schedule) {
        Customer customer = findById(schedule.getCustomer().getId());
        Customer customerToUpdate = customerRepository.getById(customer.getId());
        customerToUpdate.removeAssociation(schedule);
    }

    private Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ENTITY_NOT_FOUND)
        );
    }

    public List<CustomerDto> findAll() {
        Long tenant = tokenServices.getTenant(request);
        List<Customer> customers = customerRepository.findByTenant(tenant);
        return CustomerDto.convert(customers);
    }

    public ResponseEntity<CustomerDto> insert(CustomerForm customerForm) {
        Long tenant = tokenServices.getTenant(request);
        Customer customer = customerRepository.save(new Customer(customerForm, tenant));
        return ResponseEntity.ok().body(new CustomerDto(customer));
    }

    public ResponseEntity<List<CustomerDto>> insertAll(List<CustomerForm> customers) {
        Long tenant = tokenServices.getTenant(request);
        List<Customer> savedCustomers = customerRepository.saveAll(Customer.convert(customers, tenant));
        return ResponseEntity.ok().body(CustomerDto.convert(savedCustomers));
    }

    public ResponseEntity<?> update(CustomerForm customerForm, Long id) {
        findById(id);
        Customer customerToUpdate = customerRepository.getById(id);
        customerToUpdate.update(customerForm);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteById(Long id) {
        Customer customer = findById(id);
        checkUserCustomer(customer);
        scheduleService.removeAssociation(customer);
        customerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private void checkUserCustomer(Customer customer) {
        if(customer.isUserCostumerNotNull())
            throw new RequestNotAllowException(UPDATE_OR_DELETE_NOT_ALLOW);
    }
}
