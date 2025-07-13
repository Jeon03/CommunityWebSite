package com.community.backend.dto.Post;

import lombok.Data;

@Data
public class CommentRequest {
    private Long postId;
    private Long userId;
    private String nickname;
    private String content;
    private Long parentId;
}