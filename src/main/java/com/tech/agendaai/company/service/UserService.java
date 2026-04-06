package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.user.CreateUserRequest;
import com.tech.agendaai.company.model.user.Role;
import com.tech.agendaai.company.model.user.User;
import com.tech.agendaai.company.model.user.UserAlreadyExistException;
import com.tech.agendaai.company.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public void createUser(CreateUserRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(e -> {
            throw new UserAlreadyExistException("Email %s already exist".formatted(request.email()));
        });
        User build = User.builder()
                .name(request.name())
                .email(request.email())
                .createdAt(Instant.now())
                .role(roleParse(request.role()))
                .company(companyService.findByNickname(request.companyNickname()).orElseThrow(CompanyNotFoundException::new))
                .build();
        userRepository.save(build);
    }

    private Role roleParse(String role) {
        final String MANAGER = "MANAGER";
        final String ADMIN = "ADMIN";
        final String DEFAULT = "DEFAULT";

        return switch (role.toUpperCase()) {
            case MANAGER -> Role.MANAGER;
            case ADMIN -> Role.ADMIN;
            case DEFAULT -> Role.DEFAULT;
            default -> throw new IllegalArgumentException();
        };
    }

    public User buildUserFor(CreateUserRequest request) {
        return User.builder().name(request.name()).email(request.email()).createdAt(Instant.now()).role(Role.MANAGER).build();
    }

    public Optional<User> findUserBy(String email) {
        return userRepository.findByEmail(email);
    }
}
