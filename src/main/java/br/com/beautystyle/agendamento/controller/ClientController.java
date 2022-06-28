package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.ClientDto;
import br.com.beautystyle.agendamento.controller.form.ClientForm;
import br.com.beautystyle.agendamento.model.entity.Client;
import br.com.beautystyle.agendamento.repository.ClientRepository;
import br.com.beautystyle.agendamento.repository.CompanyRepository;
import br.com.beautystyle.agendamento.repository.EventRepository;
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

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @GetMapping("/{id}")
    public List<ClientDto> findByCompanyId(@PathVariable Long id) {
        List<Client> clientList = clientRepository.findByCompanyId(id);
        return  ClientDto.convert(clientList);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ClientDto> insert(@RequestBody @Valid ClientForm clientForm, UriComponentsBuilder uriBuilder) {
        Client savedClient = clientRepository.save(new Client(clientForm));
        URI uri = uriBuilder.path("/client/{id}")
                .buildAndExpand(savedClient.getClientId())
                .toUri();
        return ResponseEntity.created(uri).body(new ClientDto(savedClient));
    }

    @PostMapping("/client_list")
    @Transactional
    public ResponseEntity<List<ClientDto>> insertAll(@RequestBody @Valid List<ClientForm> clientList, UriComponentsBuilder uriBuilder) {
        List<Client> newClients = Client.convert(clientList);
        List<Client> savedClients = clientRepository.saveAll(newClients);
        URI uri = uriBuilder.path("/client/{id}")
                .buildAndExpand(savedClients)
                .toUri();
        return ResponseEntity.created(uri).body(ClientDto.convert(savedClients));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ClientDto> update(@RequestBody @Valid ClientDto clientDto) {
        Optional<Client> clientById = clientRepository.findById(clientDto.getApiId());
        if (clientById.isPresent()) {
            Client client = clientDto.update(clientRepository);
            return ResponseEntity.ok(new ClientDto(client));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Client> clientById = clientRepository.findById(id);
        if (clientById.isPresent()) {
            Client client = clientById.get();
            if(client.getUserId()!=null){
               client.removeAssociation(eventRepository);
                clientRepository.deleteById(id);
            }else{
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
