package com.community.backend.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String nickname;
    private Long userId;
    private LocalDateTime createdAt;
    private Long parentId;
}