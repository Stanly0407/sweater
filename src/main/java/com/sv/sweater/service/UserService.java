package com.sv.sweater.service;


import com.sv.sweater.domain.Role;
import com.sv.sweater.domain.User;
import com.sv.sweater.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    private String hostname;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString()); // генерация кода для подстверждения почты
        user.setPassword(passwordEncoder.encode(user.getPassword())); // шифр. пароль юзера

        userRepo.save(user);
        sendMessage(user);


        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            // отправка сообщений тем юзерам кто указывает почту при рег-ции. См. MailSender
            String message = String.format(
                    "Hello, %s!\n" +
                            "Welcome to Sweater. Please, visit next link: http://%s/activate/%s",
                    // в продакшене данный уже реальный адрес можно вынести в пропертис
                    user.getUsername(),
                    hostname,
                    user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        // далее метод кот. будет искать юзера в репо по активационному коду:
        User user = userRepo.findByActivationCode(code);

        if (user == null) { //если юзер не найдет = активация не удалась;
            return false;
        }
        user.setActivationCode(null); // значит пользователь подтвердил свой ящик
        userRepo.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        // Переведем роли в строки
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        // итерируем по полученному списку в поисках соот. роли
        // перед этим очищаем роли пользователя
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailGhanged = (email != null && !email.equals(userEmail)) || (userEmail != null && !userEmail.equals(email));

        if (isEmailGhanged) { //если мэйл изменился, то обновляем его у юзера:
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) { // а также актив.код устанавливаем, кот. после сохр. ниже отправим на почту
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        // проверяем установил ли юзер новый пароль - тогда устанавливаем к юзеру его
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
        // отправляем уставноленный новый актив.код (если новый мэйл)
        if (isEmailGhanged) {
            sendMessage(user);
        }
    }


    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepo.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepo.save(user);
    }
}
