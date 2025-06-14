package vn.dkk.medicineshop.controller.admin;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.URI;
import vn.dkk.medicineshop.domain.GeminiResponse;
import vn.dkk.medicineshop.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpSession;
import vn.dkk.medicineshop.domain.ChatbotQuery;
import vn.dkk.medicineshop.repository.ChatbotQueryRepository;
import vn.dkk.medicineshop.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;
import vn.dkk.medicineshop.domain.Product;
import vn.dkk.medicineshop.service.ProductService;

@Controller
public class ChatbotController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private ChatbotQueryRepository chatbotQueryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

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
        String userQuery = payload.get("query");
        User user = userService.getUserByEmail((String) session.getAttribute("email"));

        try {

            List<ChatbotQuery> history = chatbotQueryRepository.findTop5ByUserOrderByIdDesc(user);
            java.util.Collections.reverse(history);

            String firstApiResponse = callGeminiWithTools(userQuery, history);

            JsonObject responseJson = JsonParser.parseString(firstApiResponse).getAsJsonObject();
            JsonObject firstCandidate = responseJson.getAsJsonArray("candidates").get(0).getAsJsonObject();

            String finalChatbotResponse;

            if (firstCandidate.getAsJsonObject("content").has("parts") &&
                    firstCandidate.getAsJsonObject("content").get("parts").getAsJsonArray().get(0).getAsJsonObject()
                            .has("functionCall")) {

                JsonObject functionCall = firstCandidate.getAsJsonObject("content").get("parts").getAsJsonArray().get(0)
                        .getAsJsonObject().getAsJsonObject("functionCall");
                String functionName = functionCall.get("name").getAsString();
                JsonObject args = functionCall.has("args") ? functionCall.getAsJsonObject("args") : new JsonObject();

                String functionResult = executeFunctionCall(functionName, args);

                finalChatbotResponse = callGeminiWithFunctionResult(userQuery, history, functionCall, functionResult);

            } else {

                finalChatbotResponse = extractTextFromResponse(firstCandidate);
            }

            ChatbotQuery newHistory = new ChatbotQuery(userQuery, finalChatbotResponse);
            newHistory.setUser(user);
            chatbotQueryRepository.save(newHistory);
            return finalChatbotResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return "Đã có lỗi xảy ra trong quá trình xử lý: " + e.getMessage();
        }
    }

    private String callGeminiWithTools(String userQuery, List<ChatbotQuery> history) throws Exception {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key="
                + geminiApiKey;
        String conversationHistory = buildConversationHistory(history, userQuery);
        String toolsSchema = buildToolsSchema();

        String systemPrompt = "Bạn tên là MediBot, là một trợ lý tư vấn thuốc cho người dùng và có kiến thức về lĩnh vực y học. "
                +
                "Hãy trả lời một cách chính xác, thân thiện, dễ hiểu. " +
                "Hãy sử dụng các công cụ được cung cấp để tra cứu thông tin về cửa hàng và sản phẩm khi cần thiết. " +
                "Nếu người dùng hỏi về bệnh, hãy dùng công cụ tìm thuốc. Nếu hỏi về thông tin chung, hãy dùng công cụ tương ứng. "
                +
                "Tuyệt đối không được tự bịa ra thông tin không có trong kết quả từ công cụ.";

        String json = String.format(
                "{\"contents\": [%s], \"tools\": [%s], \"systemInstruction\": {\"parts\": [{\"text\": \"%s\"}]}}",
                conversationHistory,
                toolsSchema,
                escapeJson(systemPrompt));

        HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException(
                    "Lỗi kết nối đến Gemini (Tools): " + response.statusCode() + " - " + response.body());
        }
    }

    private String callGeminiWithFunctionResult(String originalUserQuery, List<ChatbotQuery> history,
            JsonObject functionCall, String functionResult) throws Exception {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key="
                + geminiApiKey;
        String conversationHistory = buildConversationHistory(history, originalUserQuery);
        String functionCallAndResult = String.format(
                ", {\"role\": \"model\", \"parts\": [{\"functionCall\": %s}]}, " +
                        "{\"role\": \"tool\", \"parts\": [{\"functionResponse\": {\"name\": \"%s\", \"response\": {\"content\": \"%s\"}}}]}",
                functionCall.toString(),
                functionCall.get("name").getAsString(),
                escapeJson(functionResult));

        String systemPrompt = "Bạn tên là MediBot, là một trợ lý tư vấn thuốc cho người dùng và có kiến thức về lĩnh vực y học. "
                +
                "Hãy trả lời một cách chính xác, thân thiện, dễ hiểu. " +
                "Hãy sử dụng các công cụ được cung cấp để tra cứu thông tin về cửa hàng và sản phẩm khi cần thiết. " +
                "Nếu người dùng hỏi về bệnh, hãy dùng công cụ tìm thuốc. Nếu hỏi về thông tin chung, hãy dùng công cụ tương ứng. "
                +
                "Tuyệt đối không được tự bịa ra thông tin không có trong kết quả từ công cụ.";

        String json = String.format(
                "{\"contents\": [%s%s], \"systemInstruction\": {\"parts\": [{\"text\": \"%s\"}]}}",
                conversationHistory,
                functionCallAndResult,
                escapeJson(systemPrompt));

        HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
            return extractTextFromResponse(responseJson.getAsJsonArray("candidates").get(0).getAsJsonObject());
        } else {
            throw new RuntimeException(
                    "Lỗi kết nối đến Gemini (Function Result): " + response.statusCode() + " - " + response.body());
        }
    }

    private String buildToolsSchema() {
        return """
                    {
                        "function_declarations": [
                            {
                                "name": "getShopInfo",
                                "description": "Lấy thông tin chung về cửa hàng, chẳng hạn như tên của shop là gì."
                            },
                            {
                                "name": "listAvailableProductsSummary",
                                "description": "Kiểm tra xem cửa hàng đang bán những mặt hàng nào, có bao nhiêu loại sản phẩm và liệt kê một vài ví dụ."
                            },
                            {
                                "name": "getProductDetails",
                                "description": "Lấy thông tin chi tiết, công dụng và giá bán của một loại thuốc cụ thể.",
                                "parameters": {
                                    "type": "OBJECT",
                                    "properties": { "productName": { "type": "STRING", "description": "Tên của sản phẩm cần tra cứu." } },
                                    "required": ["productName"]
                                }
                            },
                            {
                                "name": "getAvailableMedicinesBySymptoms",
                                "description": "Khi người dùng mô tả một triệu chứng hoặc bệnh, hãy dùng công cụ này. Nó sẽ tự động suy luận ra các loại thuốc phù hợp và kiểm tra xem chúng có sẵn trong cửa hàng hay không.",
                                "parameters": {
                                    "type": "OBJECT",
                                    "properties": { "symptomDescription": { "type": "STRING", "description": "Mô tả đầy đủ của người dùng về triệu chứng hoặc bệnh, ví dụ: 'tôi bị ho khan và rát họng'." } },
                                    "required": ["symptomDescription"]
                                }
                            }
                        ]
                    }
                """;
    }

    private String executeFunctionCall(String functionName, JsonObject args) throws Exception {
        switch (functionName) {
            case "getShopInfo":
                return productService.getShopInfo();

            case "listAvailableProductsSummary":
                return productService.listAvailableProductsSummary();

            case "getProductDetails":
                String productName = args.has("productName") ? args.get("productName").getAsString() : "";
                return productService.getProductDetails(productName);

            case "getAvailableMedicinesBySymptoms":
                String symptomDescription = args.has("symptomDescription")
                        ? args.get("symptomDescription").getAsString()
                        : "";

                List<Product> allProducts = productService.getAllProduct();
                String productCatalog = allProducts.stream()
                        .map(p -> String.format("{'Tên thuốc': '%s', 'Mô tả và công dụng': '%s'}",
                                escapeJson(p.getName()),
                                escapeJson(p.getDescription())))
                        .collect(Collectors.joining(", "));

                String selectionPrompt = String.format(
                        "Đóng vai là một dược sĩ. Dựa vào danh sách TẤT CẢ các loại thuốc có sẵn sau đây: [%s]. " +
                                "Bây giờ, hãy phân tích triệu chứng của bệnh nhân: '%s'. " +
                                "Nhiệm vụ của bạn là chọn ra những sản phẩm PHÙ HỢP NHẤT từ danh sách đã cho. " +
                                "Chỉ trả lời dưới dạng một mảng JSON chứa tên các sản phẩm bạn đã chọn. Ví dụ: [\"Aerius\", \"Paracetamol\"]",
                        productCatalog,
                        escapeJson(symptomDescription));

                String selectedNamesJson = callGeminiForSuggestions(selectionPrompt);

                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> selectedNames = gson.fromJson(selectedNamesJson, listType);

                if (selectedNames == null || selectedNames.isEmpty()) {
                    return "Sau khi phân tích, tôi không tìm thấy sản phẩm nào thực sự phù hợp với triệu chứng của bạn trong cửa hàng.";
                }

                List<Product> finalProducts = productService.filterSuggestedProducts(selectedNames);

                String productInfo = finalProducts.stream()
                        .map(p -> String.format("{'Tên thuốc': '%s', 'Công dụng': '%s'}",
                                escapeJson(p.getName()),
                                escapeJson(p.getDescription())))
                        .collect(Collectors.joining(", "));

                return "Dựa trên phân tích, đây là danh sách các sản phẩm phù hợp có sẵn tại cửa hàng: [" + productInfo
                        + "]";

            default:
                return "Không tìm thấy công cụ phù hợp.";
        }
    }

    private String buildConversationHistory(List<ChatbotQuery> history, String userQuery) {
        StringBuilder builder = new StringBuilder();
        for (ChatbotQuery q : history) {
            if (builder.length() > 0)
                builder.append(", ");
            builder.append("{\"role\": \"user\", \"parts\": [{\"text\": \"").append(escapeJson(q.getQuery()))
                    .append("\"}]}");
            builder.append(", {\"role\": \"model\", \"parts\": [{\"text\": \"").append(escapeJson(q.getResponse()))
                    .append("\"}]}");
        }
        if (userQuery != null && !userQuery.isEmpty()) {
            if (builder.length() > 0)
                builder.append(", ");
            builder.append("{\"role\": \"user\", \"parts\": [{\"text\": \"").append(escapeJson(userQuery))
                    .append("\"}]}");
        }
        return builder.toString();
    }

    private String callGeminiForSuggestions(String prompt) throws Exception {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key="
                + geminiApiKey;
        String configJson = "\"generationConfig\": {\"responseMimeType\": \"application/json\"}";

        String requestBodyJson = String.format(
                "{ \"contents\": [{\"role\": \"user\", \"parts\": [{\"text\": \"%s\"}]}], %s }",
                escapeJson(prompt),
                configJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
            String rawText = responseJson.getAsJsonArray("candidates").get(0).getAsJsonObject()
                    .getAsJsonObject("content").getAsJsonArray("parts").get(0).getAsJsonObject()
                    .get("text").getAsString();
            return rawText;
        }
        return "[]";
    }

    private String extractTextFromResponse(JsonObject candidate) {
        if (candidate != null && candidate.has("content")) {
            JsonObject content = candidate.getAsJsonObject("content");
            if (content.has("parts")) {
                JsonArray parts = content.getAsJsonArray("parts");
                if (!parts.isEmpty() && parts.get(0).getAsJsonObject().has("text")) {
                    return parts.get(0).getAsJsonObject().get("text").getAsString();
                }
            }
        }
        return "Tôi chưa có câu trả lời cho vấn đề này. Bạn có thể hỏi câu khác được không?";
    }

    private String escapeJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "").replace("\t",
                "\\t");
    }

    // clear history chat
    @GetMapping("/chat-history/delete")
    public String getMethodName(Model model, HttpSession session) {
        long userId = (long) session.getAttribute("id");
        List<ChatbotQuery> chatHistory = chatbotQueryRepository.findByUserId(userId);
        model.addAttribute("chatHistory", chatHistory);
        return "client/chatbot/delete-chat";
    }

    @PostMapping("/chat-history/delete/y")
    public String deleteHistoryChat(Model model,
            HttpSession session) {
        long userId = (long) session.getAttribute("id");
        List<ChatbotQuery> chatHistory = chatbotQueryRepository.findByUserId(userId);
        this.chatbotQueryRepository.deleteAll(chatHistory);
        return "redirect:/chatbox";
    }
}
