<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <title>Chatbot</title>
            <link rel="stylesheet" href="/client/css/chat.css">

        </head>

        <body>
            <h1>Chat with Gemini!</h1>

            <div id="chat-history">
                <c:forEach items="${chatHistory}" var="entry">
                    <p><strong>You:</strong> ${entry.query}</p>
                    <p><strong>Bot:</strong> ${entry.response}</p>
                </c:forEach>
            </div>

            <div class="chat-input-container">
                <input type="text" id="query" placeholder="Enter your question">
                <button onclick="sendMessage()">Send</button>
            </div>


            <script>
                let chatHistory = [];

                function sendMessage() {
                    const query = document.getElementById("query").value.trim();
                    document.getElementById("query").value = "";

                    if (!query) return;

                    fetch('/chat', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ query: query })
                    })
                        .then(response => response.text())
                        .then(data => {
                            displayMessage(query, data);
                            chatHistory.push({ query: query, response: data });
                            localStorage.setItem('chatHistory', JSON.stringify(chatHistory));
                        })
                        .catch(error => {
                            console.error("Lỗi gửi tin nhắn:", error);
                            displayMessage(query, "[Lỗi khi gọi chatbot]");
                        });
                }

                function displayMessage(query, response) {
                    let chatHistoryDiv = document.getElementById("chat-history");
                    chatHistoryDiv.innerHTML += "<p><strong>You:</strong> " + query + "</p>";
                    chatHistoryDiv.innerHTML += "<p><strong>Bot:</strong> " + response + "</p>";
                }

                window.onload = function () {
                    let storedHistory = localStorage.getItem('chatHistory');
                    if (storedHistory) {
                        chatHistory = JSON.parse(storedHistory);
                        chatHistory.forEach(entry => {
                            displayMessage(entry.query, entry.response);
                        });
                    }
                };
            </script>
        </body>

        </html>