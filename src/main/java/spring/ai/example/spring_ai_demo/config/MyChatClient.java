package spring.ai.example.spring_ai_demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.ai.example.spring_ai_demo.tools.SummaryTools;

import java.util.List;

@Configuration
public class MyChatClient {
 /*   @Bean
    public ChatModel chatModel(OllamaApi ollamaApi,
                               SummaryTools summaryTools) {

        ToolCallingManager manager =
                DefaultToolCallingManager.instantiate(List.of(summaryTools));
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(OllamaOptions.builder()
                        .temperature(0.3).build())
                .toolCallingManager(summaryTools)
                .build();
    }*/
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

    @Bean
    public SummaryTools summaryTools() {
        return new SummaryTools();
    }
}