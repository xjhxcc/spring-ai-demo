package spring.ai.example.spring_ai_demo.email;

import jakarta.mail.BodyPart;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;

public class EmlParser {

    // 主要方法：从类路径（resources目录）下的文件路径解析
    public static String parseEmlFromClasspath(String classPath) throws Exception {
        // 使用 Spring 的 ResourceLoader 机制获取资源
        Resource resource = new ClassPathResource(classPath);
        try (InputStream inputStream = resource.getInputStream()) {
            return parseEmlFromStream(inputStream);
        }
    }

    // 核心方法：从 InputStream 解析
    public static String parseEmlFromStream(InputStream inputStream) throws Exception {
        Session session = Session.getDefaultInstance(System.getProperties(), null);
        MimeMessage message = new MimeMessage(session, inputStream);

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Subject: ").append(message.getSubject()).append("\n\n");
        emailContent.append("From: ").append(message.getFrom()[0].toString()).append("\n\n");
        emailContent.append("Date: ").append(message.getSentDate()).append("\n\n");
        emailContent.append("Body: \n");

        Object content = message.getContent();
        String bodyText = getTextFromContent(content);
        emailContent.append(bodyText);

        return emailContent.toString();
    }

    // 辅助方法：递归地从邮件内容中提取文本（与之前相同）
    private static String getTextFromContent(Object content) throws Exception {
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) content;
            StringBuilder textBuilder = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    textBuilder.append(getTextFromContent(bodyPart.getContent()));
                    textBuilder.append("\n");
                } else if (bodyPart.isMimeType("text/html")) {
                    // 可选：处理HTML部分
                    // String html = (String) bodyPart.getContent();
                    // textBuilder.append(Jsoup.parse(html).text());
                } else if (bodyPart.getContent() instanceof MimeMultipart) {
                    textBuilder.append(getTextFromContent(bodyPart.getContent()));
                }
            }
            return textBuilder.toString();
        }
        return "";
    }
}