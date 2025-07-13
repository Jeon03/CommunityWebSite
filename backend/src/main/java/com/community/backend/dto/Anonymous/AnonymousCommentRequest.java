package com.community.backend.dto.Anonymous;

import lombok.Data;

@Data
public class AnonymousCommentRequest {
    private Long postId;
    private Long parentId; // optional
    private String content;
}
