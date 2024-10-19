package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.message.MessageReq;
import com.finalProject.linkedin.dto.responce.message.MessageResp;
import com.finalProject.linkedin.entity.Message;
import com.finalProject.linkedin.repository.MessageRepository;
import com.finalProject.linkedin.service.serviceIR.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ModelMapper modelMapper;
    private final MessageRepository messageRepository;

    @Override
    public MessageResp create(MessageReq messageReq) {
        Message message = modelMapper.map(messageReq, Message.class);
        if (getIdFromEntity(message) == 0L) {
            Message savedMessage = messageRepository.save(message);
            return modelMapper.map(savedMessage, MessageResp.class);
        }
        throw new RuntimeException("Chat already exist");
    }

    @Override
    public boolean deleteById(Long id) {
        if (messageRepository.existsById(id)) {
            Message message = getOne(id);
            message.setDeletedAt(LocalDateTime.now());
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    @Override
    public Message getOne(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<MessageResp> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable).map(message -> modelMapper.map(message, MessageResp.class)).toList();
    }

    @Override
    public MessageResp update(Long id, MessageReq MessageReq) {
        return null;
    }


    public long getIdFromEntity(Message message) {
        Optional<Message> chat1 = messageRepository.findAll().stream().filter(c -> c.equals(message)).findFirst();
        return chat1.map(Message::getChatId).orElse(0L);
    }
}
