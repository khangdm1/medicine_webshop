package vn.dkk.medicineshop.repository;

import java.util.List;
import vn.dkk.medicineshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dkk.medicineshop.domain.ChatbotQuery;

@Repository
public interface ChatbotQueryRepository extends JpaRepository<ChatbotQuery, Long> {
    List<ChatbotQuery> findByUserId(long userId);

    // Lấy 5 câu hỏi gần nhất của user, sắp xếp theo id giảm dần
    List<ChatbotQuery> findTop5ByUserOrderByIdDesc(User user);

}
