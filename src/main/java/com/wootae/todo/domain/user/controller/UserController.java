package com.wootae.todo.domain.user.controller;

import com.wootae.todo.domain.user.dto.UserDTO;
import com.wootae.todo.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String showJoinPage() {
        return "user/join";
    }

    @PostMapping("/join")
    public String join(@Valid UserDTO.JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
                return "user/join";
            }
            userService.join(joinRequest);

            return "redirect:/user/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/join";
        }
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login";
    }


}
