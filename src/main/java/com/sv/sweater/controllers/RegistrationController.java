package com.sv.sweater.controllers;

import com.sv.sweater.domain.User;
import com.sv.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        // проверяем верно ли повторно юзер ввел пароль
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordError", "passwords are different!");
        }
 // если в bindingResult будут ошибки - обрабатываем их
        if(bindingResult.hasErrors()){
            //переводим в текст. коллект.
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    // обработка подтверждения аккаунта пользователя
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        // сообщаем юзеру о том как прошла активация
        if (isActivated) {
            model.addAttribute("message", "User successfully activated.");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }


}
