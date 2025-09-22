package spring.ai.example.spring_ai_demo.email;

import org.springframework.stereotype.Service;

@Service
public class ResourceEmailService {

    private static final String EMAIL_DIRECTORY = "emails/";

    public String loadAndParseEmail(String filename) throws Exception {
        String fullPath = EMAIL_DIRECTORY + filename;
        return EmlParser.parseEmlFromClasspath(fullPath);
    }
}