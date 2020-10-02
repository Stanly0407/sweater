package com.sv.sweater.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path")
    private String uploadPath;

    @Bean //бин, кот. будет возвращать рест тэйсплэйт (настройка ответа от гугловых сервисов)
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///D:/Projects/sweater/uploads/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        // classpath: - ищет в директории проекта, file:  - ищнт в системе (но мне не помогло и пришлось путь писать выше полностью)
    }


}