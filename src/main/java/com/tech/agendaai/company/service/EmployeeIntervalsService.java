package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.interval.*;
import com.tech.agendaai.company.model.user.User;
import com.tech.agendaai.company.model.user.UserNotFoundException;
import com.tech.agendaai.company.repository.LunchIntervalRepository;
import com.tech.agendaai.company.utils.JwtHandler;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class EmployeeIntervalsService {

    private final LunchIntervalRepository lunchIntervalRepository;
    private final UserService userService;
    private final JwtHandler jwtHandler;


    public EmployeeIntervalsService(LunchIntervalRepository lunchIntervalRepository, UserService userService, JwtHandler jwtHandler) {
        this.lunchIntervalRepository = lunchIntervalRepository;
        this.userService = userService;
        this.jwtHandler = jwtHandler;
    }

    public CreateIntervalResponse createInterval(IntervalCreateRequest request) {
        Jwt currentUser = jwtHandler.getCurrentUser();
        String email = currentUser.getClaimAsString("email");

        User user = userService.findUserByEmail(email).orElseThrow(UserNotFoundException::new);

        Intervals build = Intervals.builder()
                .start(request.start())
                .finish(request.end())
                .lunchStart(request.lunchStart())
                .lunchEnd(request.lunchEnd())
                .user(user)
                .build();

        user.setIntervals(build);

        Intervals saved = lunchIntervalRepository.save(build);

        return new CreateIntervalResponse(saved.getId(), saved.getUser().getPublicId());
    }

    // refactor this method based on the return type
    @Transactional
    public void changeInterval(Long id, ChangeIntervalRequest request) {
        Intervals intervals = lunchIntervalRepository.findById(id).orElseThrow();

        intervals.setStart(request.start());
        intervals.setFinish(request.end());
        intervals.setLunchStart(request.lunchStart());
        intervals.setLunchEnd(request.lunchEnd());
    }


    // refactor get interval based on the employee not the company
    public LunchIntervalResponse getInterval(Long companyId) {
        return lunchIntervalRepository.getInterval(companyId);
    }
}
