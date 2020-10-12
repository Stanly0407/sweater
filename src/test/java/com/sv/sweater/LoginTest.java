package com.sv.sweater;

import com.sv.sweater.controllers.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc //теперь спринг автоматически пытается создать структуру классов, которая подменяет слой MVC
// (даст более удобный метод тестирования приложения - все будет происходить в фэйковом окружении - это быстрее, контролируемо и проще)
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    // По гайду заинжектить контроллер
    @Autowired
    private MainController controller;

    @Test // аннотация помечает  тестовые методы
    public void test() throws Exception {
        this.mockMvc.perform(get("/")) //вызываем у mockMVC метод perform, которым показываем,
                // что мы хотим выполнить get запрос - / на главную стр проекта.
                .andDo(print())               // будет выводить полученный результат в консоль - м.б. поломки.
                .andExpect(status().isOk())   // обертка наж assertThat методом - позволяет сравнить результат,
                // кот. возвращается с тестируемым кодом с тем рез-том, кот. мы ожидаем
                // И бросит исключение если что-то не так. И здесь мы ожидаем код возврата на http - 200
                .andExpect(content().string(containsString("Hello, guest!")));
        //проверяем что вернеться какой-то контент. И этот контент мы как строку сравниваем что
        // содержит в себе подстроку

    }

    @Test
}




