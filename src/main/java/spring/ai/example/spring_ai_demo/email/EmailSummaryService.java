package spring.ai.example.spring_ai_demo.email;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailSummaryService {

    private final ChatClient chatClient; // Spring AI 自动注入

    @Autowired
    public EmailSummaryService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String summarizeEmailThread(String emailThreadContent) {
        // 1. 构建一个强大的 Prompt 模板
        PromptTemplate promptTemplate = new PromptTemplate("""
                你是一个专业的邮件摘要助手。请严格遵循以下步骤分析邮件线程：
                 1.  分析整个邮件的讨论过程和核心议题。
                 2.  提取关键决策、结论和行动项（Action Items）。
                 3.  识别每个行动项的负责人（如有）。
                 4.  使用中文输出，保持客观、简洁。

                 请为以下邮件线程生成一份结构化摘要：
                 {emailContent}

                 【输出格式要求】：
                 **核心议题：**
                 ...这里总结核心问题...

                 **讨论过程与关键节点：**
                 - [时间] [发件人]: 关键点...

                 **最终结论与行动项：**
                 - **行动项1** (负责人: [姓名]): [具体任务]

                 **待办事项：**
                 - ...
                """);

        // 2. 将邮件内容作为变量传入模板，创建最终的 Prompt 对象
        Prompt prompt = promptTemplate.create(Map.of("emailContent", emailThreadContent));

        // 3. 调用模型并返回结果
        return chatClient.prompt(prompt).call().content();
    }
}