package com.gosia.multiplication.model;

import java.time.Duration;


public class QuizValidation {


    public static long durationSeconds(Quiz form, Quiz formBack) {
        Duration duration = Duration.between(form.getQuestions().get(0).getTimeOfQuestion(),
                formBack.getQuestions().get(0).getTimeOfQuestion());
        return duration.getSeconds();
    }

    public static long durationNano(Quiz form, Quiz formBack) {
        Duration duration = Duration.between(form.getQuestions().get(0).getTimeOfQuestion(),
                formBack.getQuestions().get(0).getTimeOfQuestion());
        return duration.getNano();
    }

    public static void IsAnswerCorrect(Quiz form, Quiz formBack) {
        for (int i = 0; i < 10; i++) {
            if (form.getQuestions().get(i).getAnswer().equals(formBack.getQuestions().get(i).getAnswerUser())) {
                formBack.getQuestions().get(i).setCorrect(Boolean.TRUE);
            }
        }
    }

    public static Boolean isQuizCorrect(Quiz formBack) {
        Boolean pass = false;
        Integer score = 0;
        for (int i = 0; i < 10; i++) {
            if (formBack.getQuestions().get(i).isCorrect()) score++;
        }
        if (score == 10) pass = true;
        return pass;
    }

    public static Boolean isQuizOnTime(long durationSeconds, Integer limit) {
        Boolean pass = false;
        if (durationSeconds <= limit) pass = true;
        return pass;
    }

}
