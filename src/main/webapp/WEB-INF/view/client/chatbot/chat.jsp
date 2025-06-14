<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Chatbot</title>
    <link rel="stylesheet" href="/client/css/chat.css">
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
    <style>
        body {
            font-family: Arial;
            margin: 20px;
            background-color: #f0f2f5;
        }

        h3 {
            text-align: center;
        }

        .chat-history-box {
            border: 1px solid #ccc;
            padding: 10px 5px; 
            height: 400px;
            background: #ffffff;
            border-radius: 10px;
            display: flex;
            flex-direction: column;
            overflow-y: auto;
        }

        .chat-bubble {
            max-width: 70%;
            padding: 10px 15px;
            border-radius: 18px;
            margin: 10px 0;
            line-height: 1.4;
            word-wrap: break-word;
        }

        .bot-message {
            background-color: #e4e6eb;
            text-align: left;
            float: left;
            clear: both;
            border-bottom-left-radius: 0;
            align-self: flex-start;
        }

        .user-message {
            background-color: #37b24d;
            color: white;
            text-align: left;
            float: right;
            clear: both;
            border-bottom-right-radius: 0;
            align-self: flex-end;
        }

        .chat-input-container {
            margin-top: 15px;
            display: flex;
            gap: 10px;
        }

        #query {
            flex: 1;
            padding: 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        
        /* Thêm style cho spinner để nó không làm vỡ layout */
        .spinner {
            float: left;
            clear: both;
            border: 4px solid #f3f3f3; 
            border-top: 4px solid #37b24d; /* Dùng màu xanh của bạn */
            border-radius: 50%;
            width: 25px;
            height: 25px;
            animation: spin 1s linear infinite;
            display: none; /* Ẩn ban đầu */
            margin: 10px 0;
        }
        @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
    </style>
</head>

<body>
    <h3>Trợ lí ảo - MediBot!</h3>

    <div class="chat-history-box" id="chat-history-box">
        <c:forEach var="chat" items="${chatHistory}">
            <div class="chat-bubble user-message">${chat.query}</div>
            <div class="chat-bubble bot-message"><c:out value="${chat.response}" escapeXml="false"/></div>
        </c:forEach>
    </div>

    <div class="chat-input-container">
        <input type="text" id="query" placeholder="Nhập câu hỏi của bạn...">
        <button id="send-btn" onclick="sendMessage()">Send</button>
        <a href="/chat-history/delete" class="clear-button" onclick="return confirm('Bạn có chắc muốn xóa toàn bộ lịch sử chat?')">Clear</a>
    </div>

    <script>
        const chatBox = document.getElementById("chat-history-box");
        const queryInput = document.getElementById("query");
        const sendBtn = document.getElementById("send-btn");

        // Hàm tự động cuộn xuống cuối
        function scrollToBottom() {
            chatBox.scrollTop = chatBox.scrollHeight;
        }

        // Tự động cuộn xuống khi tải trang xong
        window.onload = function () {
            scrollToBottom();

            // Parse Markdown cho tất cả bot message
            document.querySelectorAll(".bot-message").forEach(div => {
                div.innerHTML = marked.parse(div.innerHTML);
            });
        };
        
        // Gửi tin nhắn khi nhấn Enter
        queryInput.addEventListener("keypress", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                sendMessage();
            }
        });

        function sendMessage() {
            const query = queryInput.value.trim();
            if (!query) return;

            // Xóa input và vô hiệu hóa nút gửi
            queryInput.value = "";
            sendBtn.disabled = true;

            // Hiển thị tin nhắn của người dùng ngay lập tức
            appendMessage(query, 'user-message');

            // Hiển thị spinner "đang chờ"
            const spinner = appendSpinner();

            fetch('/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ query: query })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Lỗi mạng hoặc server: ' + response.statusText);
                }
                return response.text();
            })
            .then(data => {
                // Xóa spinner và hiển thị câu trả lời của bot
                spinner.remove();
                // Render câu trả lời dưới dạng Markdown để có định dạng đẹp (in đậm, danh sách...)
                appendMessage(marked.parse(data), 'bot-message', true);
            })
            .catch(error => {
                console.error("Lỗi gửi tin nhắn:", error);
                spinner.remove();
                appendMessage("[Rất tiếc, đã có lỗi xảy ra. Vui lòng thử lại sau.]", 'bot-message');
            })
            .finally(() => {
                // Kích hoạt lại nút gửi
                sendBtn.disabled = false;
                queryInput.focus();
            });
        }

        // Hàm chung để thêm tin nhắn vào giao diện
        function appendMessage(content, className, isHtml = false) {
            const bubble = document.createElement("div");
            bubble.className = "chat-bubble " + className;
            if (isHtml) {
                bubble.innerHTML = content;
            } else {
                bubble.textContent = content;
            }
            chatBox.appendChild(bubble);
            scrollToBottom();
        }

        // Hàm để thêm spinner
        function appendSpinner() {
            const spinnerDiv = document.createElement("div");
            spinnerDiv.className = "spinner";
            chatBox.appendChild(spinnerDiv);
            scrollToBottom();
            return spinnerDiv;
        }
    </script>
</body>

</html>