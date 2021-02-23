package com.gosia.multiplication;

import com.gosia.multiplication.security.UserDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class QuestionController {

    private final ScoreService scoreService;

    Quiz form = new Quiz();

    public QuestionController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        form.getQuestions().clear();
        levelBasic();
        model.addAttribute("form", form);
        return "levelBasic";
    }

    @GetMapping("/master")
    public String master(Model model) {
        form.getQuestions().clear();
        levelMaster();
        model.addAttribute("form", form);
        return "levelMaster";
    }

    @PostMapping("/resultBasic")
    public String resultBasic(Model model, @ModelAttribute Quiz formBack) {
        try {
            IsAnswerCorrect(form, formBack);
            model.addAttribute("formBack", formBack);
            model.addAttribute("time", durationSeconds(form, formBack) + " sekund " + durationNano(form, formBack) + " nanosekund ");
            Integer limit = 40;
            isQuizCorrect(formBack); //czy na wszystkie pytania padła poprawna odpowiedź
            isQuizOnTime(durationSeconds(form, formBack), limit); //czy quiz wykonany w czasie
            model.addAttribute("isQuizCorrect", isQuizCorrect(formBack));
            model.addAttribute("isQuizOnTime", isQuizOnTime(durationSeconds(form, formBack), limit));

            if (isQuizCorrect(formBack) && isQuizOnTime(durationSeconds(form, formBack), limit)) {
                AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                UserDTO currentUser = (UserDTO) authenticationToken.getPrincipal();
                Score score = new Score(currentUser.getEmail(), formBack.getQuestions().get(0).getTimeOfQuestion(), durationSeconds(form, formBack), durationNano(form, formBack), 'B');
                scoreService.save(score);
            }

            form.getQuestions().clear();
            return "result";

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "redirect:/basic";
        }
    }

    @GetMapping("/rankingBasic")
    public String rankingBasic(Model model) {
        model.addAttribute("top", getRankingBasic());
        return "ranking";
    }

    @GetMapping("/rankingMaster")
    public String rankingMaster(Model model) {
        model.addAttribute("top", getRankingMaster());
        return "ranking";
    }

    @PostMapping("/resultMaster")
    public String resultMaster(Model model, @ModelAttribute Quiz formBack) {
        try {
            IsAnswerCorrect(form, formBack);
            model.addAttribute("formBack", formBack);
            model.addAttribute("time", durationSeconds(form, formBack) + " sekund " + durationNano(form, formBack) + " nanosekund ");
            Integer limit = 60;
            isQuizCorrect(formBack); //czy na wszystkie pytania padła poprawna odpowiedź
            isQuizOnTime(durationSeconds(form, formBack), limit); //czy quiz wykonany w czasie
            model.addAttribute("isQuizCorrect", isQuizCorrect(formBack));
            model.addAttribute("isQuizOnTime", isQuizOnTime(durationSeconds(form, formBack), limit));
            if (isQuizCorrect(formBack) && isQuizOnTime(durationSeconds(form, formBack), limit)) {
                AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                UserDTO currentUser = (UserDTO) authenticationToken.getPrincipal();
                Score score = new Score(currentUser.getEmail(), formBack.getQuestions().get(0).getTimeOfQuestion(), durationSeconds(form, formBack), durationNano(form, formBack), 'M');
                scoreService.save(score);
            }
            form.getQuestions().clear();
            return "result";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "redirect:/master";
        }
    }

    private Quiz levelBasic() {
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


    private Quiz levelMaster() {
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

    private long durationSeconds(Quiz form, Quiz formBack) {
        Duration duration = Duration.between(form.getQuestions().get(0).getTimeOfQuestion(),
                formBack.getQuestions().get(0).getTimeOfQuestion());
        return duration.getSeconds();
    }

    private long durationNano(Quiz form, Quiz formBack) {
        Duration duration = Duration.between(form.getQuestions().get(0).getTimeOfQuestion(),
                formBack.getQuestions().get(0).getTimeOfQuestion());
        return duration.getNano();
    }

    private void IsAnswerCorrect(Quiz form, Quiz formBack) {
        for (int i = 0; i < 10; i++) {
            if (form.getQuestions().get(i).getAnswer() == formBack.getQuestions().get(i).getAnswerUser()) {
                formBack.getQuestions().get(i).setCorrect(Boolean.TRUE);
            }
        }
    }

    private Boolean isQuizCorrect(Quiz formBack) {
        Boolean pass = false;
        Integer score = 0;
        for (int i = 0; i < 10; i++) {
            if (formBack.getQuestions().get(i).isCorrect()) score++;
        }
        if (score == 10) pass = true;
        return pass;
    }

    private Boolean isQuizOnTime(long durationSeconds, Integer limit) {
        Boolean pass = false;
        if (durationSeconds <= limit) pass = true;
        return pass;
    }

    private List<Score> getRankingBasic() {
        return scoreService.getScoreList().stream().filter(f -> f.getLevel() == 'B').sorted(Comparator.comparingLong(Score::getDurationSecond)).
                sorted(Comparator.comparingLong(Score::getDurationNano)).collect(Collectors.toList());
    }

    private List<Score> getRankingMaster() {
        return scoreService.getScoreList().stream().filter(f -> f.getLevel() == 'M').sorted(Comparator.comparingLong(Score::getDurationSecond)).
                sorted(Comparator.comparingLong(Score::getDurationNano)).collect(Collectors.toList());
    }
}

