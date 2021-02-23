package com.gosia.multiplication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ScoreService {

    private final ScoreRepository scoreRepository;


    public void save(final Score score) {
        scoreRepository.save(score);
    }

    public Set<Score> getScoreList() {
        return scoreRepository.findAll().stream().collect(Collectors.toSet());
    }
}
