package com.sv.sweater;

import com.sv.sweater.controllers.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Интеграционные тесты

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc //теперь спринг автоматически пытается создать структуру классов, которая подменяет слой MVC
// (даст более удобный метод тестирования приложения - все будет происходить в фэйковом окружении - это быстрее, контролируемо и проще)
@TestPropertySource("/application-test.properties")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    // По гайду заинжектить контроллер
    @Autowired
    private MainController controller;

    @Test // аннотация помечает  тестовые методы
    public void contextLoads() throws Exception {
        this.mockMvc.perform(get("/")) //вызываем у mockMVC метод perform, которым показываем,
                // что мы хотим выполнить get запрос - / на главную стр проекта.
                .andDo(print())               // будет выводить полученный результат в консоль - м.б. поломки.
                .andExpect(status().isOk())   // обертка наж assertThat методом - позволяет сравнить результат,
                // кот. возвращается с тестируемым кодом с тем рез-том, кот. мы ожидаем
                // И бросит исключение если что-то не так. И здесь мы ожидаем код возврата на http - 200
                .andExpect(content().string(containsString("Hello, guest!")))
        //проверяем что вернеться какой-то контент. И этот контент мы как строку сравниваем что
        // содержит в себе подстроку
                .andExpect(content().string(containsString("Please, sign in")));
    }

    @Test
    public void accessDeniedTest() throws Exception {
     // метод для проверкт авторизации
        this.mockMvc.perform(get("/main")) //адрес странички, кот.  требует авторизации
                    .andDo(print())
                    .andExpect(status().is3xxRedirection()) // проверяем, что система ожидает статус отличный от 200
                    .andExpect(redirectedUrl("http://localhost/login"));  //что система нам подкинет необходимый адрес
    }

    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = { "/create-user-after.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void correctLoginTest() throws Exception{ //проверяем авторизацию пользователя
        this.mockMvc.perform(formLogin().user("Admin").password("a"))  //(зависимости спец.) Метод смотрит как мы в контексте определили login page и вызывает обращение к этой страничке
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
    }

      //проверка, что у нас отрабатывает отбивка на неверные данные юзера
    @Test
    public void badCredentials() throws Exception{
        this.mockMvc.perform(post("/login").param("user","Alyona"))
                .andDo(print()) // в консоль то что у нас вернул сервер
                .andExpect(status().isForbidden()); //ожидаем статус
    }


}




