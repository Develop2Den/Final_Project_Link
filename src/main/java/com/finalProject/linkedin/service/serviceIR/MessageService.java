package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import com.finalProject.linkedin.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    MessageResp create(MessageReq messageReq);

    boolean deleteById(Long id);

    Message getOne(Long id);

    List<MessageResp> findAll(Pageable pageable);

    MessageResp update(Long id, MessageReq MessageReq);
}
