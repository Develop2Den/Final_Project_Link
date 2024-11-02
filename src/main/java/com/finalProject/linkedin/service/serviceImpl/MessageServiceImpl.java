package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.GetMessageWithProfileResp;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import com.finalProject.linkedin.entity.Message;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.MessageMapper;
import com.finalProject.linkedin.repository.MessageRepository;
import com.finalProject.linkedin.repository.UserRepository;
import com.finalProject.linkedin.service.serviceIR.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResp create(MessageReq messageReq) {
        return messageMapper.toMessageResp(messageRepository.save(messageMapper.toMessage(messageReq)));
    }

    @Override
    public boolean deleteById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Message not found with id " + id));
        message.setDeletedAt(LocalDateTime.now());
        messageRepository.save(message);
        return true;
    }


    @Override
    public List<MessageResp> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable).map(messageMapper::toMessageResp).toList();
    }



    @Override
    public List<MessageResp> getChatMessages(Long id1, Long id2, Pageable pageable) {
        if (userRepository.findById(id1).isEmpty())
            throw new NotFoundException("User not found with id " + id1);
        if (userRepository.findById(id2).isEmpty())
            throw new NotFoundException("User not found with id " + id2);
        return messageRepository.findMessagesBetweenUsers(id1, id2, pageable).stream().map(messageMapper::toMessageResp).toList();
    }

    @Override
    public List<GetMessageWithProfileResp> findLatestMessagesForUser(Long id, Pageable pageable) {
        if (userRepository.findById(id).isEmpty())
            throw new NotFoundException("User not found with id " + id);
        return messageRepository.findLatestMessagesForEachPairByUserId(id, pageable).stream()
                .map(message -> messageMapper.toMessageWithUserResp(message, id))
                .toList();
    }


}
