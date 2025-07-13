package com.community.backend.dto.Anonymous;

import lombok.Data;

@Data
public class AnonymousPostRequest {
    private String title;
    private String content;
    private Long writerId;
    private String imageUrl;
}