package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.chat.ChatReq;
import com.finalProject.linkedin.dto.responce.chat.ChatResp;
import com.finalProject.linkedin.entity.Chat;
import com.finalProject.linkedin.repository.ChatRepository;
import com.finalProject.linkedin.service.serviceIR.ChatService;
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
public class ChatServiceImpl implements ChatService {

    private final ModelMapper modelMapper;
    private final ChatRepository chatRepository;

    @Override
    public ChatResp create(ChatReq chatReq) {
        Chat chat = modelMapper.map(chatReq, Chat.class);
        if (getIdFromEntity(chat) == 0L) {
            Chat savedChat = chatRepository.save(chat);
            return modelMapper.map(savedChat, ChatResp.class);
        }
        throw new RuntimeException("Chat already exist");
    }

    @Override
    public boolean deleteById(Long id) {
        if (chatRepository.existsById(id)) {
            Chat chat = getOne(id);
            chat.setDeletedAt(LocalDateTime.now());
            chatRepository.save(chat);
            return true;
        }
        return false;
    }

    @Override
    public Chat getOne(long id) {
        return chatRepository.findById(id).orElse(null);
    }

    @Override
    public List<ChatResp> findAll(Pageable pageable) {
        return chatRepository.findAll(pageable).map(chat -> modelMapper.map(chat, ChatResp.class)).toList();
    }



    public long getIdFromEntity(Chat chat) {
        Optional<Chat> chat1 = chatRepository.findAll().stream().filter(c -> c.equals(chat)).findFirst();
        return chat1.map(Chat::getChatId).orElse(0L);
    }
}
