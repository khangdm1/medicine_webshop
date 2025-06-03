package vn.dkk.medicineshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dkk.medicineshop.domain.ChatbotQuery;

@Repository
public interface ChatbotQueryRepository extends JpaRepository<ChatbotQuery, Long> {
    List<ChatbotQuery> findByUserId(long userId);
}
