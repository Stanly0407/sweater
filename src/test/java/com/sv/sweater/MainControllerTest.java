package com.sv.sweater;

import com.sv.sweater.controllers.MessageController;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

//Интеграционные тесты

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("Admin") // м. перед методом указывать, как и ост. ниже аннотации
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //т.к. БД для тестов пустая - наполним
@Sql(value = {"/messages-list-after.sql", "/create-user-after.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // в обратном порядке
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageController controller;

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
                .andExpect(xpath("//div[@id='message-list']/div").nodeCount(4)); //ожидаем, что возвращ. будет не строку, а кол-во узлов
        // на практике для таких тестов удобнее иметь БД для тестов - создаем, см. application-test.properties и необх. аннотация @TestPropertySource

    }

    @Test
    public void filterMessageTest() throws Exception{ //проверка фильтрации по тегу "my-tag" (В БД для тестов с этим тэгом сообщения под id 1 и 3)
        this.mockMvc.perform(get("/main").param("filter", "my-tag"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='message-list']/div").nodeCount(2))
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=1]").exists())
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=3]").exists());
    }

    @Test
    public void addMessageToListTest() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .file("file", "123".getBytes())
                .param("text", "fifth")
                .param("tag", "new one")
                .with(csrf());
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='message-list']/div").nodeCount(5)) // это будет месседж с id = 5 и элементов будет выведено 5
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=10]").exists()) // т.к. нумерацию установили с 10
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=10]/div/span").string("fifth"))
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=10]/div/i").string("#new one"));
    }


}
