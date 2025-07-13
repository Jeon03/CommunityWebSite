package com.community.backend.dto;


import com.community.backend.dto.Anonymous.AnonymousPostResponse;
import com.community.backend.dto.Post.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {
    private List<PostResponse> freePosts;
    private List<AnonymousPostResponse> anonymousPosts;
    private List<PostResponse> marketPosts;
}