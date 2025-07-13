package com.community.backend.dto.Anonymous;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnonymousCommentResponse {
    private Long id;
    private String content;
    private String anonymousName;
    private Long parentId;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Long userId;
}
