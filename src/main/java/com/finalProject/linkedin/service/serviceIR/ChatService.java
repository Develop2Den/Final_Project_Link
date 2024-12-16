package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChatService {
    Chat create(Chat chat);

    boolean deleteById(Long id);

    Chat getOne(long id);

    Page<Chat> findAll(Pageable pageable);


}
