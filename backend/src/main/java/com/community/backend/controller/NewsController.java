package com.community.backend.controller;

import com.community.backend.dto.NewsDto;
import com.community.backend.service.NewsService;
import com.community.backend.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final OpenAiService openAiService;

    @GetMapping
    public List<NewsDto> getNews() {
        return newsService.getLatestNews();
    }

    @PostMapping("/summary")
    public String summarize(@RequestBody NewsDto news) {
        String prompt = "다음 뉴스를 최대 10줄 이내로 요약해줘:\n\n" + news.getContent();
        return openAiService.summarize(prompt);
    }
}
