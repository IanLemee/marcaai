package com.tech.agendaai.company.repository;

import com.tech.agendaai.company.model.user.Employees;
import com.tech.agendaai.company.model.user.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @NativeQuery(value = """
                        select u.name, i.start, i.finish, i.lunch_start, i.lunch_end \s
                        from users u \s
                            join intervals i on u.interval_id = i.id \s
                            join company c on u.company_id = c.id where c.nickname = ?1
           \s""")
    List<Employees> findAllEmployees(@NotNull String companyId);

    Optional<User> findByPublicId(UUID publicId);
}
