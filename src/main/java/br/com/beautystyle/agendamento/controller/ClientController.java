package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.form.ClientForm;
import br.com.beautystyle.agendamento.model.Client;
import br.com.beautystyle.agendamento.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Client> findAll() {
        return  clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        Optional<Client> clientById = clientRepository.findById(id);
        return clientById
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Client> insert(@RequestBody @Valid ClientForm clientForm, UriComponentsBuilder uriBuilder) {
        Client savedClient = clientRepository.save(clientForm.converter());
        URI uri = uriBuilder.path("/client/{id}")
                .buildAndExpand(savedClient.getClientId())
                .toUri();
        return ResponseEntity.created(uri).body(savedClient);
    }

    @PostMapping("/salva_lista")
    @Transactional
    public ResponseEntity<List<Client>> insertAll(@RequestBody @Valid List<ClientForm> clientList, UriComponentsBuilder uriBuilder) {
        List<Client> newClients = new ArrayList<>();
        clientList.forEach(client -> newClients.add(client.converter()));
        List<Client> savedClientList = clientRepository.saveAll(newClients);
        URI uri = uriBuilder.path("/client/{id}")
                .buildAndExpand(savedClientList)
                .toUri();
        return ResponseEntity.created(uri).body(savedClientList);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Client> update(@RequestBody @Valid Client client) {
        Optional<Client> clientById = clientRepository.findById(client.getClientId());
        if (clientById.isPresent()) {
            Client update = client.update(clientRepository);
            return ResponseEntity.ok(update);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Client> clientById = clientRepository.findById(id);
        if (clientById.isPresent()) {
            clientRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
