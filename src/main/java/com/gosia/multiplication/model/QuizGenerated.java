package com.gosia.multiplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizGenerated {


    public static Quiz levelBasic() {
        Quiz form = new Quiz();
        List<Integer> answers = new ArrayList<>(); //lista pomocnicza odpowiedzi, aby uniknac dubli w pytaniach
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            Question question = new Question();
            question.setId(i + 1);
            do {
                question.setNumber1(random.nextInt(4) + 6); //6,7,8,9
                question.setNumber2(random.nextInt(8) + 2);//2-9
                question.setAnswer(question.getNumber1() * question.getNumber2());
            } while (answers.contains(question.getAnswer()));
            answers.add(question.getAnswer());
            form.addQuestion(question);
        }
        return form;
    }

    public static Quiz levelMaster() {
        Quiz form = new Quiz();
        List<Integer> answers = new ArrayList<>(); //lista pomocnicza odpowiedzi, aby uniknac dubli w pytaniach
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            Question question = new Question();
            question.setId(i + 1);
            do {
                question.setNumber1(random.nextInt(89) + 11); //11-99
                question.setNumber2(random.nextInt(8) + 2);//2-9
                question.setAnswer(question.getNumber1() * question.getNumber2());
            } while (answers.contains(question.getAnswer()));
            answers.add(question.getAnswer());
            form.addQuestion(question);
        }
        return form;
    }

}
