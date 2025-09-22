package spring.ai.example.spring_ai_demo.email;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MyController {

    private final ChatClient chatClient;

    MyController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/description")
    String getDescription() {
        return chatClient.prompt()
                .user(u -> u.text("Explain what do you see on this picture?")
                        .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("/multimodal.test.png")))
                .call()
                .content();
    }

    @GetMapping("/ai")
    String generation(@RequestParam String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    /*// 示例：假设已配置了ChatMemory
    String responseWithMemory = chatClient.prompt()
            .user(newMessage)
            .advisors(a -> a.param("chat_memory_conversation_id", "conversation-123"))
            .call()
            .content();*/
}
