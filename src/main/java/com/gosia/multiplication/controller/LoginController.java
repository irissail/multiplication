package com.gosia.multiplication.controller;

import com.gosia.multiplication.model.UserDTO;
import com.gosia.multiplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("showErrorExistUser", Boolean.FALSE);
        return "registration";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/save_user")
    public String saveUser(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "registration";
        }

        try {
            userService.registerUser(userDTO);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", userDTO);
            model.addAttribute("showErrorExistUser", Boolean.TRUE);
            return "registration";
        }
        return "redirect:/login";
    }

}

