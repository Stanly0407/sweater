package com.sv.sweater.repositories;

import com.sv.sweater.domain.Message;
import com.sv.sweater.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface MessageRepo extends CrudRepository<Message, Long> {

    Page<Message> findAll(Pageable pageable);
    Page<Message> findByTag(String tag, Pageable pageable); // добавляем отображение длинных списков с разбивкой на страницы (pagination).

    // HQL
    @Query("from Message m where  m.author = :author")
    Page<Message> findByUser(Pageable pageable, @Param("author") User author);
}
