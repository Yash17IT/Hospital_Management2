
package org.example.aiproject2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        excludeName = {
                "org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration"
        }
)
public class AiProject2Application {

    public static void main(String[] args) {
        SpringApplication.run(AiProject2Application.class, args);
    }
}
