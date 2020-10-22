package com.sv.sweater.controllers;

import com.sv.sweater.domain.Message;
import com.sv.sweater.domain.User;
import com.sv.sweater.repositories.MessageRepo;
import com.sv.sweater.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Controller
public class MessageController {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private MessageService messageService;

    @Value("${upload.path}") // ищет в пропертис путь и вставляет в переменную
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
            /*Зачастую, при построении сайтов и вэб приложений нет неоходимости отображать списки элементов целиком,
            тем более, что список может быть очень длинным, что негативно скажется работе браузера и сервера.
            В такой ситуации используется постраничный вывод данных (пагинация, pagination).*/
            ) {
        Page<Message> page = messageService.messageList(pageable, filter);

        if (filter != null && !filter.isEmpty()) {
            page = messageRepo.findByTag(filter, pageable);
        } else {
            page = messageRepo.findAll(pageable);
        }

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message, //аннотация кот. запускает валидацию
            BindingResult bindingResult, //список аргументов и сообщений ошибок валидации
            //!!!!!!! Данные выше аргументы ВСЕГДА д. идти выше Model! Иначе ошибки валидации попадут в представление
            Model model,
            @RequestParam("file") MultipartFile file)
            throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);  // ошибка отобразится в представлении - см. main.ftlh
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);// сохраняем файл. если bindingResult не содержит ошибок, то тогда БД сохраняет результат
            model.addAttribute("message", null); // в случае если валидация прошла успешно- удалит из модели месседж, иначе
            //    после добавления мы получим открытую форму с сообщением
            messageRepo.save(message);
        }

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    public void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) { //если директории нет - то ее создаст
                uploadDir.mkdir();
            }
            // создаем уникальное имя файла, чтобы обезопасить себя от коллизий:
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
            ) {
        Page<Message> page = messageService.messageListForUser(pageable, currentUser, author); //получаем месседжи и кладем в модель
        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser)); //определяем явл. ли текущий юзеро подписчиком того юзера на страницу кот. он зашел
        model.addAttribute("page", page);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("url", "/user-messages" + author.getId());
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        //проверка безопасности, чтоб юзер мог менять только свои сообщения
        if (message.getAuthor().equals(currentUser)) {
            //проверка на непустое поле
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            saveFile(message, file);
            messageRepo.save(message);
        }

        return "redirect:/user-messages/" + user;
    }

}