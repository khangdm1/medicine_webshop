package vn.dkk.medicineshop.controller.admin;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.net.URI;
import vn.dkk.medicineshop.domain.GeminiResponse;
import vn.dkk.medicineshop.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpSession;
import vn.dkk.medicineshop.domain.ChatbotQuery;
import vn.dkk.medicineshop.repository.ChatbotQueryRepository;
import vn.dkk.medicineshop.service.UserService;

@Controller
public class ChatbotController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private ChatbotQueryRepository chatbotQueryRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/chatbox")
    public String index(Model model, HttpSession session) {
        long userId = (long) session.getAttribute("id");
        List<ChatbotQuery> chatHistory = chatbotQueryRepository.findByUserId(userId);
        model.addAttribute("chatHistory", chatHistory);
        return "client/chatbot/chat";
    }

    @PostMapping(value = "/chat", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String chat(@RequestBody Map<String, String> payload, HttpSession session) {
        String query = payload.get("query");

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key="
                    + geminiApiKey;

            HttpClient client = HttpClient.newHttpClient();
            Gson gson = new Gson();

            String json = String.format("{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}", query);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                GeminiResponse geminiResponse = gson.fromJson(response.body(), GeminiResponse.class);

                if (geminiResponse.getCandidates() != null &&
                        !geminiResponse.getCandidates().isEmpty() &&
                        geminiResponse.getCandidates().get(0).getContent() != null &&
                        geminiResponse.getCandidates().get(0).getContent().getParts() != null &&
                        !geminiResponse.getCandidates().get(0).getContent().getParts().isEmpty()) {

                    String chatbotResponse = geminiResponse.getCandidates().get(0).getContent().getParts().get(0)
                            .getText();

                    // Save to database
                    String email = (String) session.getAttribute("email");
                    User user = this.userService.getUserByEmail(email);
                    ChatbotQuery history = new ChatbotQuery(query, chatbotResponse);
                    history.setUser(user);
                    chatbotQueryRepository.save(history);

                    return chatbotResponse;
                }

                return "Bot không có phản hồi cho câu hỏi này.";
            } else {
                return "Lỗi kết nối đến Gemini: " + response.statusCode();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi xử lý phản hồi từ Gemini: " + e.getMessage();
        }
    }
}
