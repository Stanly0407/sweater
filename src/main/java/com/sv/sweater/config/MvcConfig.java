package com.sv.sweater.config;

import com.sv.sweater.util.RedirectInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

    @Override // Чтобы  <#if (p - 1) == page.getNumber()> в pager  работал корректно и страницы не с 0 считались
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true);
        argumentResolvers.add(resolver);
        WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RedirectInterceptor());
    }
}