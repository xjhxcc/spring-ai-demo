package spring.ai.example.spring_ai_demo.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/emails")
public class EmailSummaryController {

    @Autowired
    private EmailSummaryService emailSummaryService; // 你的摘要服务

    @Autowired
    private ResourceEmailService resourceEmailService; // 新增的资源邮件服务

    // 通过URL参数指定要解析的邮件文件名
    // 示例请求: GET /api/emails/summarize?filename=commodity_missing_contact_number.eml
    @GetMapping("/summarize")
    public String summarizeEmlFile(@RequestParam String filename) {
        try {
            // 1. 从 resources/emails/ 加载并解析指定的 .eml 文件
            String extractedEmailText = resourceEmailService.loadAndParseEmail(filename);

            // 2. 将提取出的文本交给您的摘要服务
            return emailSummaryService.summarizeEmailThread(extractedEmailText);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing .eml file: " + e.getMessage();
        }
    }

    @GetMapping("genRepliedEmail")
    public String genRepliedEmail(@RequestParam String orderId) throws Exception {
        return emailSummaryService.generateFollowUp(orderId);
    }

    @GetMapping(value = "genRepliedEmail2")
    public Flux<String> genRepliedEmail2(@RequestParam String orderId) throws Exception {
        return emailSummaryService.generateFollowUp2(orderId);
    }

    @GetMapping(value = "genRepliedEmail3")
    public Flux<String> genRepliedEmail3(@RequestParam String orderId) throws Exception {
        return emailSummaryService.generateFollowUp3(orderId);
    }

}

