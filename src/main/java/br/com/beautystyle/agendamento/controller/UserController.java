package br.com.beautystyle.agendamento.controller;

import br.com.beautystyle.agendamento.config.security.TokenServices;
import br.com.beautystyle.agendamento.controller.form.UserForm;
import br.com.beautystyle.agendamento.controller.form.UserProfessionalForm;
import br.com.beautystyle.agendamento.model.entity.*;
import br.com.beautystyle.agendamento.repository.ProfileRepository;
import br.com.beautystyle.agendamento.repository.ScheduleRepository;
import br.com.beautystyle.agendamento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static br.com.beautystyle.agendamento.controller.ConstantsController.USER_PROFILE_C;
import static br.com.beautystyle.agendamento.controller.ConstantsController.USER_PROFILE_P;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TokenServices tokenServices;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PostMapping("/professional")
    @Transactional
    public ResponseEntity<?> insertProfessionalUser(@RequestBody @Valid UserProfessionalForm userForm) {
        Profiles profile = profileRepository.findByProfileName(USER_PROFILE_P);
        UserProfessional professionalUser = userForm.converter(profile);
        userRepository.save(professionalUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customer")
    @Transactional
    public ResponseEntity<?> insertCostumerUser(@RequestBody @Valid UserForm userForm) {
        Profiles profile = profileRepository.findByProfileName(USER_PROFILE_C);
        UserCustomer costumerUser = userForm.convertCostumer(profile);
        userRepository.save(costumerUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid UserForm userForm,
                                    HttpServletRequest request) {
        Long userId = tokenServices.getUserId(request);
        try {
            userForm.update(userId, userRepository);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/professional")
    @Transactional
    public ResponseEntity<?> removeUserProfessional(HttpServletRequest request) {
        Long userId = tokenServices.getUserId(request);
        try {
            userRepository.deleteById(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/customer")
    @Transactional
    public ResponseEntity<?> removeUserCostumer(HttpServletRequest request) {
        Long userId = tokenServices.getUserId(request);
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                List<Schedule> schedules = scheduleRepository.findByCustomerUserCustomerId(userId);
                //schedules.forEach(schedule -> schedule.removeCostumerAssociation(scheduleRepository));
                userRepository.deleteById(userId);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
