package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.controller.dto.UserDto;
import br.com.beautystyle.agendamento.model.entity.User;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserDto> insert(@RequestBody @Valid UserDto userDto, UriComponentsBuilder uriBuilder) {
        User savedUser = userRepository.save(userDto.converter());
        URI uri = uriBuilder.path("/user/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new UserDto(savedUser));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getApiId());
        if (user.isPresent()) {
            User update = userDto.update(userRepository);
            return ResponseEntity.ok(new UserDto(update));
        }
        return ResponseEntity.notFound().build();
    }

}
