package com.tech.agendaai.company.service;

import com.tech.agendaai.company.service.abacatepay.dto.CustomerRequestAbacatePay;
import com.tech.agendaai.company.service.abacatepay.dto.CustomerRequestMetadata;
import com.tech.agendaai.company.service.abacatepay.dto.CustomerResponseAbacatePay;
import com.tech.agendaai.company.model.user.*;
import com.tech.agendaai.company.repository.UserRepository;
import com.tech.agendaai.company.service.abacatepay.AbacatePayHttpClient;
import com.tech.agendaai.company.utils.JwtHandler;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtHandler jwtHandler;
    private final AbacatePayHttpClient abacatePayHttpClient;

    private static final String CUSTOMER_URL_PATH = "customers/create";
    private static final String CUSTOMER_TYPE_EMPLOYEE = "employee";
    private static final String MANAGER = "MANAGER";
    private static final String ADMIN = "ADMIN";
    private static final String DEFAULT = "DEFAULT";

    public UserService(UserRepository userRepository, JwtHandler jwtHandler, AbacatePayHttpClient abacatePayHttpClient) {
        this.userRepository = userRepository;
        this.jwtHandler = jwtHandler;
        this.abacatePayHttpClient = abacatePayHttpClient;
    }

    public UserResponse createUser() {
        Jwt currentUser = currentUser();
        String email = currentUser.getClaimAsString("email").toLowerCase();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new UserAlreadyExistException();
        }

        User build = User.builder()
                .name(currentUser.getClaimAsString("preferred_username").toLowerCase())
                .email(email)
                .createdAt(Instant.now())
                .role(Role.ADMIN)
                .build();

        String customerPublicId = sendToAbacatePay(build);
        build.setPublicId(customerPublicId);
        User saved = userRepository.save(build);

        return new UserResponse(saved.getName(), saved.getRole(), saved.getPublicId());
    }

    private String sendToAbacatePay(User user) {
        CustomerRequestMetadata metadata = new CustomerRequestMetadata(CUSTOMER_TYPE_EMPLOYEE, user.getCompany().getPublicId().toString());
        CustomerRequestAbacatePay request = new CustomerRequestAbacatePay(user.getEmail(), user.getEmail(), "", user.getPhoneNumber(), metadata);
        CustomerResponseAbacatePay send = abacatePayHttpClient.send(request, CustomerResponseAbacatePay.class, CUSTOMER_URL_PATH);
        return send.id();
    }

    public Jwt currentUser() {
        return jwtHandler.getCurrentUser();
    }

    private Role roleParse(String role) {

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

    public Optional<User> findUserByPublicId(String id) {
        return userRepository.findByPublicId(UUID.fromString(id));
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<Employees> findAllEmployeesAndIntervalsByCompanyNickname(@NotNull String companyNickname) {
        return userRepository.findAllEmployees(companyNickname);
    }

    @Transactional
    public void updateUserName(String name) {
        String email = currentUser().getClaimAsString("email");
        User user = findUserByEmail(email.toLowerCase()).orElseThrow(UserNotFoundException::new);
        if (user.getName().equals(name)) return;
        user.setName(name);
    }

    public void deleteUserById(String publicId) {
        String email = currentUser().getClaimAsString("email");
        User user = findUserByEmail(email.toLowerCase()).orElseThrow(UserNotFoundException::new);

        User user1 = findUserByPublicId(publicId).orElseThrow(UserNotFoundException::new);

        if (!(user.getRole().equals(Role.MANAGER) && user.getCompany().equals(user1.getCompany()))) throw new RuntimeException("Sem permissao");

        userRepository.deleteById(user1.getId());
    }
}
