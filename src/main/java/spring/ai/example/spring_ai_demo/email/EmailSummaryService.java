package spring.ai.example.spring_ai_demo.email;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class EmailSummaryService {

    private static final Logger log = LoggerFactory.getLogger(EmailSummaryService.class);
    private final ChatClient chatClient; // Spring AI 自动注入
    private final OllamaChatModel ollamaChatModel;

    @Autowired
    public EmailSummaryService(ChatClient chatClient, OllamaChatModel ollamaChatModel, OllamaChatModel chatModel) {
        this.chatClient = chatClient;
        this.ollamaChatModel = ollamaChatModel;
    }

    public String summarizeEmailThread(String emailThreadContent) {
        // 1. 构建一个强大的 Prompt 模板
        PromptTemplate promptTemplate = new PromptTemplate("""
                你是一个专业的邮件摘要助手。请严格遵循以下步骤分析邮件线程， 这份内容可能是一封邮件，也有可能是来回沟通的多封邮件，如果是多封邮件，请分析每封邮件的主要内容，了解沟通的主题与结果：
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

    public String generateFollowUp(String orderId) throws Exception {
        List<String> emails = getAllEmailsByOrderId(orderId);
        var prompt = buildPrompt(emails);
        return chatClient.prompt(prompt).call().content();
    }

    public Flux<String> generateFollowUp2(String orderId) throws Exception {
        List<String> emails = getAllEmailsByOrderId(orderId);
        var prompt = buildPrompt(emails);
      return chatClient.prompt(prompt).stream().content();
    }

    public Flux<String> generateFollowUp3(String orderId) throws Exception {
        List<String> emails = getAllEmailsByOrderId(orderId);
        List<String> list = emails.stream().map(this::summarize).toList();
        var prompt = buildPrompt(list);
        return chatClient.prompt(prompt).stream().content();
    }


    private Prompt buildPrompt(List<String> list) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                你是一名电商客服助理，请根据以下邮件往来记录,一封邮件里面会包含着来往记录，判断客户是否已澄清所有问题。
                email content:
                {list}
                若客户已回答全部提及要素，直接返回“无需追问”。
                如果没有全部澄清，请生成一段礼貌、简洁的追问邮件（中文），要求客户补充未澄清的信息。
                    仅能对“用户原文中明确提到需要客户补充”的要素进行追问
                    禁止新增用户未提及的要素（如订单号、电话等)
                    输出格式：主题：<主题>正文：<正文>未澄清问题：<列表>
                """);
        return promptTemplate.create(Map.of("list", list));
    }


    private static final int MAX_CHARS = 50 * 2;   // ≈50 token
    public String summarize(String text) {
        if (text.length() < MAX_CHARS) return text;

        Prompt prompt = new Prompt("用50字以内总结内容：" + text);

        ChatOptions options = OllamaOptions.builder()
                .numPredict(80)
                .temperature(0.3)
                .build();

        return chatClient.prompt(prompt)
                .options(options)
                .user(text)
                .call()
                .content();
    }

    @Autowired
    private ResourceEmailService resourceEmailService; // 新增的资源邮件服务


    public List<String> getAllEmailsByOrderId(String orderId) throws Exception {
        String extractedEmailText = resourceEmailService.loadAndParseEmail("RE_commodity_missing_address_and_customer_name.eml");
//        String extractedEmailText2 = resourceEmailService.loadAndParseEmail("RE_commodity_missing_address_and_customer_name.eml");
        return List.of(extractedEmailText);
    }
}