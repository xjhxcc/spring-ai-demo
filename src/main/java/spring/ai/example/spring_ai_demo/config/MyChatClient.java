package spring.ai.example.spring_ai_demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyChatClient {

    @Bean
    public ChatClient chatClient(OllamaChatModel chatModel) {
        // 此处的 ChatModel 已由 Spring AI Ollama Starter 自动配置为 OllamaChatModel
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                    你是一个乐于助人的AI助手。
                    请用中文回答所有问题，回答要简洁明了。
                    """)
                .build();
    }
}