package spring.ai.example.spring_ai_demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ChatClient chatClient(OllamaChatModel chatModel) {
        // 此处的 ChatModel 已由 Spring AI Ollama Starter 自动配置为 OllamaChatModel
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                    你是一个乐于助人的AI助手。
                    请用中文回答所有问题，回答要简洁明了。
                    """) // 设置默认的系统消息，定义AI的角色:cite[3]:cite[9]
                // .defaultOptions(...) // 可以设置默认的模型参数，如temperature等:cite[3]
                .build();
    }
}