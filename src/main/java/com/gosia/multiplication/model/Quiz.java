package com.gosia.multiplication.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Quiz {
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

}
