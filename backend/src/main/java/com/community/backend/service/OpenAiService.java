package com.community.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final Dotenv dotenv = Dotenv.load(); // .env에서 로딩

    public String summarize(String content) {
        String apiKey = dotenv.get("OPENAI_API_KEY");

        //웹클라이언트 생성
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();

        //요청객체 구성
        Map<String, Object> request = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "다음 뉴스를 최대 10줄 이내로 요약해줘."),
                        Map.of("role", "user", "content", content)
                )
        );


        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("choices").get(0).get("message").get("content").asText())
                .block();
    }
}
