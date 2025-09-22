package spring.ai.example.spring_ai_demo.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailSummaryRequest {
    private String emailThreadContent;
}
