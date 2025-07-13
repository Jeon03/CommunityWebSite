package com.community.backend.service;

import com.community.backend.dto.NewsDto;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    private final Dotenv dotenv = Dotenv.load();
    private final String API_KEY = dotenv.get("NEWS_API_KEY");

    //해외뉴스 불러오기
    public List<NewsDto> getLatestNews() {
        if (API_KEY == null || API_KEY.isBlank()) {
            System.err.println("NEWS_API_KEY 환경변수를 불러오지 못했습니다.");
            return List.of(); // 빈 리스트 반환
        }

        String url = "https://newsapi.org/v2/top-headlines?country=us&pageSize=10&apiKey=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        JsonNode body = response.getBody();
        JsonNode articles = body != null ? body.get("articles") : null;

        if (articles == null || !articles.isArray()) {
            System.err.println("NewsAPI 응답에서 articles를 찾을 수 없습니다.");
            return List.of();
        }

        List<NewsDto> result = new ArrayList<>();
        for (JsonNode article : articles) {
            result.add(new NewsDto(
                    article.get("title").asText("제목 없음"),
                    article.get("description").asText("설명 없음"),
                    article.get("urlToImage").asText(null),
                    article.get("url").asText("#"),
                    article.get("content").asText(null)
            ));
        }

        return result;
    }

}