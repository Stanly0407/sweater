package com.sv.sweater.service;


import com.sv.sweater.domain.Role;
import com.sv.sweater.domain.User;
import com.sv.sweater.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender  mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString()); // генерация кода для подстверждения почты
        userRepo.save(user);
        if(!StringUtils.isEmpty(user.getEmail())){
            // отправка сообщений тем юзерам кто указывает почту при рег-ции. См. MailSender
            String message = String.format(
                    "Hello, %s!\n" +
                            "Welcome to Sweater. Please, visit next link: http://localhost:8080/activate/%s",
                    // в продакшене данный уже реальный адрес можно вынести в пропертис
                    user.getUsername(), user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);

        }
        return true;
    }

}
