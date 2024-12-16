package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.entity.Chat;
import com.finalProject.linkedin.exception.InvalidRequestException;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.repository.ChatRepository;
import com.finalProject.linkedin.repository.MessageRepository;
import com.finalProject.linkedin.repository.UserRepository;
import com.finalProject.linkedin.service.serviceIR.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    public Chat create(Chat chat) {
        userVerification(chat.getSenderId());
        userVerification(chat.getRecipientId());
        Optional<Chat> chatExist = chatRepository.findChatByParticipants(chat.getSenderId(), chat.getRecipientId());
        if (chatExist.isPresent())
            throw new InvalidRequestException("Chat already exist ");
        else
            return chatRepository.save(chat);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        Optional<Chat> chatExist = chatRepository.findById(id);
        if (chatExist.isPresent()) {
            Chat chat = chatExist.get();
            chat.setDeletedAt(LocalDateTime.now());
            messageRepository.markMessagesAsDeleted(id, LocalDateTime.now());
            chatRepository.save(chat);
            return true;
        }
        return false;
    }

    @Override
    public Chat getOne(long id) {
        Optional<Chat> chatExist = chatRepository.findById(id);
        if (chatExist.isEmpty() || chatExist.get().getDeletedAt() != null)
            throw new NotFoundException("Chat  not found with id= " + id);
        return chatExist.get();
    }

    @Override
    public Page<Chat> findAll(Pageable pageable) {
        return chatRepository.findAll(pageable);
    }

    private void userVerification(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) throw new NotFoundException("User not found id= " + userId);
    }

}
