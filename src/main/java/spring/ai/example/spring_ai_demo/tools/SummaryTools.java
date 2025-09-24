package spring.ai.example.spring_ai_demo.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SummaryTools {
   /* @Autowired
    private ChatClient chatClient;

    *//** 本地摘要工具：供模型自己调用 *//*
    @Tool(description = "对客户长回复进行50字以内摘要，保留关键事实")
    public String summarize(
            @ToolParam(description = "原始客户回复，>500 字时才需要调用") String text) {
        if (text.length() < 500) return text;
        PromptTemplate pt = new PromptTemplate(
                "用50字以内总结客户回答，保留数字/地址/发票号等关键信息：{text}");
        Prompt prompt = pt.create(Map.of("text", text));
        var resp = chatClient.prompt(prompt).call().chatResponse();
        return resp.getResult().getOutput().getText();
    }

    private static final int MAX_CHARS = 50 * 2;   // ≈50 token
    public String doSummary(String text) {
        if (text.length() < MAX_CHARS) return text;

        Prompt prompt = new Prompt("用50字以内总结客户回答：" + text);

        ChatOptions options = OllamaOptions.builder()
                .numPredict(80)
                .temperature(0.3)
                .build();

        return chatClient.prompt(prompt)
                .options(options)
                .user(text)
                .call()
                .content();
    }*/
}