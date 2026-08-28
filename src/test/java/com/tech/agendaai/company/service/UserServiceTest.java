package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.company.Company;
import com.tech.agendaai.company.model.company.CompanyNotFoundException;
import com.tech.agendaai.company.model.company.Plan;
import com.tech.agendaai.company.model.user.CreateUserRequest;
import com.tech.agendaai.company.model.user.Role;
import com.tech.agendaai.company.model.user.User;
import com.tech.agendaai.company.model.user.UserAlreadyExistException;
import com.tech.agendaai.company.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyService companyService;

    @InjectMocks
    private UserService userService;

    @Nested
    class create {
        @Test
        void createUserWhenSuccess() {
            CreateUserRequest createUserRequest =
                    new CreateUserRequest("John", "John@email.com", "12345678901", "ADMIN", "company1");
            Company company = Company.builder()
                    .name("Company")
                    .nickname("company1")
                    .plan(Plan.FREE)
                    .createdAt(Instant.now())
                    .build();
            User build = User.builder()
                    .name(createUserRequest.name())
                    .email(createUserRequest.email())
                    .createdAt(Instant.now())
                    .role(Role.ADMIN)
                    .company(company)
                    .build();
            when(userRepository.findByEmail(createUserRequest.email())).thenReturn(Optional.empty());
            when(companyService.findByNickname("company1")).thenReturn(Optional.of(company));
            when(userRepository.save(any(User.class))).thenReturn(build);

            User user = userService.createUser(createUserRequest);
            assertNotNull(user);
            verify(userRepository, times(1)).findByEmail(createUserRequest.email());
            verify(userRepository, times(1)).save(any(User.class));
            verify(companyService, times(1)).findByNickname(createUserRequest.companyNickname());
        }

        @Test
        void createUserShouldThrowUserAlreadyExistExceptionWhenEmailPresent() {
            CreateUserRequest createUserRequest =
                    new CreateUserRequest("John", "John@email.com", "12345678901", "ADMIN", "company1");

            User user = User.builder()
                    .name(createUserRequest.name())
                    .email(createUserRequest.email())
                    .createdAt(Instant.now())
                    .role(Role.ADMIN)
                    .build();

            when(userRepository.findByEmail(createUserRequest.email())).thenReturn(Optional.of(user));

            UserAlreadyExistException userAlreadyExistException = assertThrows(UserAlreadyExistException.class, () -> userService.createUser(createUserRequest));
            String message = userAlreadyExistException.getMessage();
            String expectedMessage = "Email %s already exist".formatted(createUserRequest.email());
            assertEquals(expectedMessage, message);
        }

        @Test
        void createUserShouldThrowCompanyNotFoundExceptionWhenCompanyNicknameNotFound(){
            CreateUserRequest createUserRequest =
                    new CreateUserRequest("John", "John@email.com", "12345678901", "ADMIN", "company1");

            when(userRepository.findByEmail(createUserRequest.email())).thenReturn(Optional.empty());
            when(companyService.findByNickname(anyString())).thenReturn(Optional.empty());

            CompanyNotFoundException companyNotFoundException = assertThrows(CompanyNotFoundException.class, () -> userService.createUser(createUserRequest));
            String message = companyNotFoundException.getMessage();
            String expectedMessage = "Company not found by nickname";
            assertEquals(expectedMessage, message);
        }
    }

    @Nested
    class buildUserFor {
        @Test
        void shouldBuildUserWhenSuccessful() {
            CreateUserRequest createUserRequest =
                    new CreateUserRequest("John", "John@email.com", "12345678901", "ADMIN", "company1");
            User user = userService.buildUserFor(createUserRequest);
            assertNotNull(user);
        }
    }

    @Nested
    class findUserBy {
        @Test
        void shouldReturnOptinalOfUserWhenSuccesful() {
            User user = User.builder()
                    .name("John")
                    .email("John@email.com")
                    .createdAt(Instant.now())
                    .role(Role.ADMIN)
                    .build();
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
            Optional<User> userBy = userService.findUserByEmail(user.getEmail());
            assertNotNull(userBy);
        }
    }

    @Nested
    class findAllEmployeesByCompanyId {
        //TODO
//        @Test
//        void shouldReturnListOfEmployees() {
//            List<String> expectedEmployees = List.of("John", "Mark", "Bob");
//            when(userRepository.findAllEmployes(1L)).thenReturn(expectedEmployees);
//            List<String> employees = userService.findAllEmployeesAndIntervalsByCompanyId(1L);
//            assertNotNull(employees);
//            assertEquals(expectedEmployees, employees);
//        }
    }
}