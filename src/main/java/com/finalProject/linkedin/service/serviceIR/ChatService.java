package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.request.chat.ChatReq;
import com.finalProject.linkedin.dto.responce.chat.ChatResp;
import com.finalProject.linkedin.entity.Chat;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    ChatResp create(ChatReq chatReq);

    boolean deleteById(Long id);

    Chat getOne(long id);

    List<ChatResp> findAll(Pageable pageable);


}
