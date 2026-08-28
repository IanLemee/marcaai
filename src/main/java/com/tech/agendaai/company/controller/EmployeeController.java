package com.tech.agendaai.company.controller;

import com.tech.agendaai.company.model.user.CreateUserRequest;
import com.tech.agendaai.company.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final UserService userService;

    public EmployeeController(UserService userService) {
        this.userService = userService;
    }

    //TODO INVITE EMPLOYEES
    @PostMapping(value = "/register")
    public void registerEmployee() {
        userService.createUser();
    }

    @PatchMapping(value = "/change/name")
    public ResponseEntity<Void> updateUserName(@RequestBody String name) {
        userService.updateUserName(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
