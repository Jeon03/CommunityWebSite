package com.community.backend.dto.Anonymous;

import com.community.backend.entity.Anonymous.AnonymousPost;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnonymousPostResponse {
    private Long id;
    private String title;
    private String content;
    private String anonymousName;
    private Long writerId;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likeCount;
    private boolean isLiked;

    public static AnonymousPostResponse from(AnonymousPost post, boolean isLiked) {
        return AnonymousPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .anonymousName("익명1")
                .writerId(post.getWriterId())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .likeCount(post.getLikeCount())
                .isLiked(isLiked)
                .build();
    }

    public static AnonymousPostResponse from(AnonymousPost post) {
        return from(post, false);
    }


}
