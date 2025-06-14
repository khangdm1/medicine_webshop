package vn.dkk.medicineshop.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.dkk.medicineshop.repository.ChatbotQueryRepository;

@Service
public class ChatbotQueryService {
    private final ChatbotQueryRepository chatbotQueryRepository;

    public ChatbotQueryService(ChatbotQueryRepository chatbotQueryRepository) {
        this.chatbotQueryRepository = chatbotQueryRepository;
    }

}
