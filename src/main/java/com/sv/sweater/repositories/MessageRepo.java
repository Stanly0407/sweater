package com.sv.sweater.repositories;

import com.sv.sweater.domain.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface MessageRepo extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);

}
