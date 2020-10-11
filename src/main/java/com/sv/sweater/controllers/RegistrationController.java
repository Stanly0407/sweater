package com.sv.sweater.controllers;

import com.sv.sweater.domain.User;
import com.sv.sweater.domain.dto.CaptchaResponseDto;
import com.sv.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    // URL для запроса  API (репатча) ( см. https://developers.google.com/recaptcha/docs/verify )   + ?secret=%s&response =%s
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired  // автов. бин
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse, //Проверка ответа пользователя на запрос reCAPTCHA от серверной части
            // приложения. (получить токен ответа пользователя). В пропертис - секретный ключ для обмена данными между сайтом и сервисом reCAPTCHA.
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        // в методе контроллера заполняем шаблон запроса апи репатча
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class); //после url спринг ожидает объект для запроса пост,
        // но нам нечего передавать и передаем просто пустой лист, затем необх. класс в кот. будет завернут ответ - см. dto - CaptchaResponseDto

        if (!response.isSuccess()) {
            // если ответ ен успешный сообщаем об этом юзеру
        model.addAttribute("captchaError", "Fill captcha!");
        }
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);  //верно ли повторно юзер ввел пароль
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty!");
        }
        // проверяем верно ли повторно юзер ввел пароль
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "passwords are different!");
        }
        // если в bindingResult будут ошибки - обрабатываем их
        if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
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
            model.addAttribute("messageType", "success"); //в отображении login.ftlh меняется класс бутстрапа в зависимости от сообщения - цвет
            model.addAttribute("message", "User successfully activated.");
        } else {
            model.addAttribute("messageType", "danger"); //в отображении login.ftlh меняется класс бутстрапа в зависимости от сообщения - цвет
            model.addAttribute("message", "Activation code is not found!");
        }
        return "login";
    }


}
