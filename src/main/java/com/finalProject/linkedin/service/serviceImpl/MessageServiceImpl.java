package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.responce.message.GetMessageWithProfileResp;
import com.finalProject.linkedin.entity.Chat;
import com.finalProject.linkedin.entity.Message;
import com.finalProject.linkedin.entity.Notification;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.MessageMapper;
import com.finalProject.linkedin.repository.*;
import com.finalProject.linkedin.service.serviceIR.MessageService;
import com.finalProject.linkedin.utils.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final ChatRepository chatRepository;

    @Override
    public Message create(Message message) {
        userVerification(message.getSenderId());
        userVerification(message.getRecipientId());
        messageRepository.save(message);
        System.out.println(" message service impl create " + message);
        return message;
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
    public Page<Message> findAll(Pageable pageable) {
        return messageRepository.findAll(pageable);
    }


    @Override
    public Page<Message> getChatMessages(Long id1, Long id2, Pageable pageable) {
        userVerification(id1);
        userVerification(id2);
        return messageRepository.findMessagesBetweenUsers(id1, id2, pageable);
    }

    @Override
    public List<GetMessageWithProfileResp> findLatestMessagesForUser(Long userId, Pageable pageable) {
        userVerification(userId);
        return messageRepository.findLatestMessagesForEachPairByUserId(userId, pageable).stream()
                .map(message -> {
                    GetMessageWithProfileResp response = messageMapper.toMessageWithUserResp(message, userId);
                    long unreadCount;
                    if (message.getSenderId().equals(userId))
                        unreadCount = messageRepository.countUnreadMessagesBetweenUsers(userId, message.getRecipientId());
                    else
                        unreadCount = messageRepository.countUnreadMessagesBetweenUsers(userId, message.getSenderId());
                    response.setUnreadMessagesCount(unreadCount);
                    return response;
                })
                .toList();
    }


    private void userVerification(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) throw new NotFoundException("User not found" + userId);
    }

    private void chatVerification(Long chatId) throws NotFoundException {
        if (!chatRepository.existsById(chatId)) throw new NotFoundException("Chat not found" + chatId);
    }

    @Override
    public Message createAndSendOrNotification(Message message, int path) {
        userVerification(message.getSenderId());
        userVerification(message.getRecipientId());
        checkChat(message);

        messageRepository.save(message);
        switch (path) {
            case 1, 2: {
                if (checkRecipientNotConnected(message))
                    createNotification(message);
                else sendMessageDirectly(message);
                break;
            }
            case 3: {
                if (checkRecipientNotConnected(message))
                    createNotification(message);
                break;
            }
            default:
                throw new NotFoundException("Not found logic for path from controller path= " + path);
        }
        return message;
    }

    private void sendMessageDirectly(Message message) {
        String recipientId = message.getRecipientId().toString();
        messagingTemplate.convertAndSendToUser(
                recipientId,            // identity sender
                "/queue/messages"+ message.getChatId(),      // channel
                message                 // message
        );

    }

    private boolean checkRecipientNotConnected(Message message) {
        String recipientId = message.getRecipientId().toString();
        return simpUserRegistry.getUser(recipientId) == null;
    }


    public void checkChat(Message message) {
        System.out.println("Message Service impl - check Chat begin" + message);
        if (null == message.getChatId() || message.getChatId().equals(0L)) {
            Optional<Chat> chat = chatRepository.findChatByParticipants(message.getSenderId(), message.getRecipientId());
            if (chat.isPresent())
                message.setChatId(chat.get().getChatId());
            else
                createNewChat(message);
        }
    }


    private void createNotification(Message message) {
        System.out.println("User not connected -> create notification ");
        notificationRepository.save(new Notification(
                message.getChatId(),
                message.getSenderId(),
                (message.getContent().length() > 20) ? message.getContent().substring(0, 20) : message.getContent(),
                NotificationType.MESSAGE,
                message.getRecipientId()
        ));
    }

    private void createNewChat(Message message) {
        Chat chat = chatRepository.save(new Chat(
                message.getSenderId(),
                message.getRecipientId(),
                LocalDateTime.now()
        ));
        message.setChatId(chat.getChatId());
        System.out.println("Message Service impl - create new chat" + message);
    }

    @Override
    public Page<Message> getMessagesByChatId(Long id, Pageable pageable) {
        chatVerification(id);
        return messageRepository.findByChat_ChatIdAndDeletedAtIsNullOrderByCreatedAtDesc(id, pageable);
    }

    @Override
    public boolean readTrue(Long id) {
       Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Message not found with id " + id));
        message.setRead(true);
        messageRepository.save(message);
        return true;
    }

    @Override
    public boolean readTrue(List<Long> ids) {
        List<Message> messages = messageRepository.findAllById(ids);
        if (messages.isEmpty()) {
            throw new NotFoundException("No messages found with provided ids");
        }
        messages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(messages);
        return true;
    }


}