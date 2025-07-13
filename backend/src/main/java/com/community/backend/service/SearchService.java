package com.community.backend.service;


import com.community.backend.dto.Anonymous.AnonymousPostResponse;
import com.community.backend.dto.Post.PostResponse;
import com.community.backend.dto.SearchResultDto;
import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.entity.Post.BoardType;
import com.community.backend.entity.Post.Post;
import com.community.backend.repository.AnonymousPostRepository;
import com.community.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;
    private final AnonymousPostRepository anonymousPostRepository;

    public SearchResultDto searchAll(String keyword) {
        // 자유게시판
        //BoardType.FREE 조건에 해당
        //title 또는 content에 keyword가 대소문자 구분 없이 포함된 게시글 조회
        List<Post> free = postRepository
                .findByBoardTypeAndTitleContainingIgnoreCaseOrBoardTypeAndContentContainingIgnoreCase(
                        BoardType.FREE, keyword, BoardType.FREE, keyword
                );

        // 장터게시판
        //title 또는 content에 keyword가 대소문자 구분 없이 포함된 게시글 조회
        List<Post> market = postRepository
                .findByBoardTypeAndTitleContainingIgnoreCaseOrBoardTypeAndContentContainingIgnoreCase(
                        BoardType.MARKET, keyword, BoardType.MARKET, keyword
                );

        // 익명게시판
        //title 또는 content에 keyword가 포함된 익명 게시글 검색
        List<AnonymousPost> anonymous = anonymousPostRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);

        return new SearchResultDto(
                free.stream().map(PostResponse::from).toList(),
                anonymous.stream().map(AnonymousPostResponse::from).toList(),
                market.stream().map(PostResponse::from).toList()
        );
    }
}
