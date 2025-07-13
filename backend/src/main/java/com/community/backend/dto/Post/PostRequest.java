package com.community.backend.dto.Post;


import com.community.backend.entity.Post.BoardType;
import lombok.Data;

@Data
public class PostRequest {
    private Long userId;
    private String title;
    private String content;
    private String imageUrl;
    private BoardType boardType;
}
