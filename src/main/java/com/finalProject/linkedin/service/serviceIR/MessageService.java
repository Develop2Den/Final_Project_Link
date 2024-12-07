package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.responce.message.GetMessageWithProfileResp;
import com.finalProject.linkedin.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    Message create(Message message);

    boolean deleteById(Long id);

    Page<Message> findAll(Pageable pageable);

    Page<Message> getChatMessages(Long id1, Long id2, Pageable pageable);

    List<GetMessageWithProfileResp> findLatestMessagesForUser(Long id1, Pageable pageable);

    Message createAndSendOrNotification(Message message);

    Page<Message> getMessagesByChatId(Long id, Pageable pageable);


}
