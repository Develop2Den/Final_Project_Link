package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
