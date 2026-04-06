package com.tech.agendaai.company.service;

import com.tech.agendaai.company.model.interval.LunchIntervalResponse;
import com.tech.agendaai.company.repository.LunchIntervalRepository;
import org.springframework.stereotype.Service;

@Service
public class LunchIntervalService {

    private final LunchIntervalRepository lunchIntervalRepository;

    public LunchIntervalService(LunchIntervalRepository lunchIntervalRepository) {
        this.lunchIntervalRepository = lunchIntervalRepository;
    }

    public LunchIntervalResponse getInterval(Long intervalId) {
        return lunchIntervalRepository.getInterval(intervalId);
    }
}
