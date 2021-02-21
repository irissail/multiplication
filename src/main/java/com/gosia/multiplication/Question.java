package com.gosia.multiplication;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Question {
    private Integer id;
    private Integer number1;
    private Integer number2;
    private Integer answer;
    private Integer answerUser;
    private LocalDateTime timeOfQuestion = LocalDateTime.now();
    private boolean correct;
}


