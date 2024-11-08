package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.GetMessageWithProfileResp;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    MessageResp create(MessageReq messageReq);

    boolean deleteById(Long id);

    List<MessageResp> findAll(Pageable pageable);

    List<MessageResp> getChatMessages(Long id1, Long id2, Pageable pageable);

    List<GetMessageWithProfileResp> findLatestMessagesForUser(Long id1, Pageable pageable);


}
