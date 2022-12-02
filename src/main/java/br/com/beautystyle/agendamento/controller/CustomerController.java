package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.CustomerDto;
import br.com.beautystyle.agendamento.controller.form.CustomerForm;
import br.com.beautystyle.agendamento.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerDto> findAll() {
        return customerService.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CustomerDto> insert(@RequestBody @Valid CustomerForm customerForm) {
        return customerService.insert(customerForm);
    }

    @PostMapping("/insert_all")
    @Transactional
    public ResponseEntity<List<CustomerDto>> insertAll(@RequestBody @Valid List<CustomerForm> customers) {
        return customerService.insertAll(customers);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid CustomerForm customerForm) {
        return customerService.update(customerForm, id);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return customerService.deleteById(id);
    }
}
