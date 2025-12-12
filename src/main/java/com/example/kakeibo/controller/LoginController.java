package com.example.kakeibo.controller;

import com.example.kakeibo.dto.UserRegistrationDto;
import com.example.kakeibo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegistrationDto dto,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(dto);
            redirectAttributes.addFlashAttribute("message", "ユーザー登録が完了しました。ログインしてください。");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
