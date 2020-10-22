package com.sv.sweater;

import org.junit.Assert;
import org.junit.Test;

//unit тэсты

public class SimpleTest {

    @Test // бесполезный тест для примера
    public void test(){
        int x = 2;
        int y = 23;

        // Класс Assert - для проверки корректности выполнения теста, кот. входит в состав junit (но есть и различ. библиотеки)
        Assert.assertEquals(46, x * y); //сначала пишем результат, который хотим получить!
        Assert.assertEquals(25, x + y);
    }

    // тестирование exception
    @Test(expected = ArithmeticException.class)
    public void error(){
        int i = 0;
        int il = 1 / i;
    }


}
