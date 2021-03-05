package com.gosia.multiplication.controller;

import com.gosia.multiplication.DTO.UserDTO;
import com.gosia.multiplication.model.*;
import com.gosia.multiplication.service.ScoreService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class QuestionController {

    private final ScoreService scoreService;

    public QuestionController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    private Quiz form;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        form = QuizGenerated.levelBasic();
        model.addAttribute("form", form);
        return "levelBasic";
    }

    @GetMapping("/master")
    public String master(Model model) {
        form = QuizGenerated.levelMaster();
        model.addAttribute("form", form);
        return "levelMaster";
    }

    @PostMapping("/resultBasic")
    public String resultBasic(Model model, @ModelAttribute Quiz formBack) {
        try {
            QuizValidation.IsAnswerCorrect(form, formBack);
            model.addAttribute("formBack", formBack);
            model.addAttribute("time", QuizValidation.durationSeconds(form, formBack) + " sekund " +
                    QuizValidation.durationNano(form, formBack) + " nanosekund ");
            model.addAttribute("isQuizCorrect", QuizValidation.isQuizCorrect(formBack));
            model.addAttribute("isQuizOnTime", QuizValidation.isQuizOnTime(QuizValidation.durationSeconds(form, formBack), 40));

            if (QuizValidation.isQuizCorrect(formBack) && QuizValidation.isQuizOnTime(QuizValidation.durationSeconds(form, formBack),
                    40)) {
                AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) SecurityContextHolder.getContext().
                        getAuthentication();
                UserDTO currentUser = (UserDTO) authenticationToken.getPrincipal();
                Score score = new Score(currentUser.getEmail(), formBack.getQuestions().get(0).getTimeOfQuestion(),
                        QuizValidation.durationSeconds(form, formBack), QuizValidation.durationNano(form, formBack), 'B');
                scoreService.save(score);
            }
            form.getQuestions().clear();
            return "result";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "redirect:/basic";
        }
    }

    @PostMapping("/resultMaster")
    public String resultMaster(Model model, @ModelAttribute Quiz formBack) {
        try {
            QuizValidation.IsAnswerCorrect(form, formBack);
            model.addAttribute("formBack", formBack);
            model.addAttribute("time", QuizValidation.durationSeconds(form, formBack) + " sekund " + QuizValidation.durationNano(form, formBack) + " nanosekund ");
            model.addAttribute("isQuizCorrect", QuizValidation.isQuizCorrect(formBack));
            model.addAttribute("isQuizOnTime", QuizValidation.isQuizOnTime(QuizValidation.durationSeconds(form, formBack), 60));

            if (QuizValidation.isQuizCorrect(formBack) && QuizValidation.isQuizOnTime(QuizValidation.durationSeconds(form, formBack), 60)) {
                AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                UserDTO currentUser = (UserDTO) authenticationToken.getPrincipal();
                Score score = new Score(currentUser.getEmail(), formBack.getQuestions().get(0).getTimeOfQuestion(),
                        QuizValidation.durationSeconds(form, formBack), QuizValidation.durationNano(form, formBack), 'M');
                scoreService.save(score);
            }
            form.getQuestions().clear();
            return "result";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "redirect:/master";
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

    private List<Score> getRankingBasic() {
        Comparator<Score> compareBySecond = Comparator.comparingLong(Score::getDurationSecond);
        Comparator<Score> compareByNano = Comparator.comparingLong(Score::getDurationNano);
        Comparator<Score> compareByTime = compareBySecond.thenComparing(compareByNano);

        return scoreService.getScoreList().stream().filter(f -> f.getLevel() == 'B').sorted(compareByTime).collect(Collectors.toList());
    }

    private List<Score> getRankingMaster() {
        Comparator<Score> compareBySecond = Comparator.comparingLong(Score::getDurationSecond);
        Comparator<Score> compareByNano = Comparator.comparingLong(Score::getDurationNano);
        Comparator<Score> compareByTime = compareBySecond.thenComparing(compareByNano);

        return scoreService.getScoreList().stream().filter(f -> f.getLevel() == 'M').sorted(compareByTime).collect(Collectors.toList());
    }


}




