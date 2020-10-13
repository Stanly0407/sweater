package com.sv.sweater;

import com.sv.sweater.controllers.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("Admin") // м. перед методом указывать, как и ост. ниже аннотации
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //т.к. БД для тестов пустая - наполним
@Sql(value = {"/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController controller;

    @Test
    public void mainPageTest() throws Exception{
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated()) //проверка что юзер корректно аутентифицирован - пройдет успешно,
                                            // если в контексте для текущего юзера установлена веб-сессия
        // но для работы метода - необх авторизовать пользваотеля - аннотация над классом @WithUserDetails("Admin") - имя юзера
                .andExpect(xpath("//div[@id='navbarSupportedContent']/div").string("Admin"));
        //данный xpath (из кода страницы) показывает, что в нашем дереве, кот. пришло в .andExpect(authenticated()) элемент, кот. имеет абрибут с
        //именем id и этот атрибут имеет след. значение 'navbarSupportedContent'
    }

    @Test
    public void messageListTest() throws Exception{ //проверка корректного отображения  списка сообщений
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated()) // снова проверяем, что пользователь аутентифицирован
                .andExpect(xpath("").nodeCount(0)); //ожидаем, что возвращ. будет не строку, а кол-во узлов
        // на практике для таких тестов удобнее иметь БД для тестов - создаем, см. application-test.properties и необх. аннотация @TestPropertySource

    }

}
