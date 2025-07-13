package com.community.backend.service;

import com.community.backend.dto.Anonymous.AnonymousPostResponse;
import com.community.backend.dto.Post.PostResponse;
import com.community.backend.entity.Anonymous.AnonymousComment;
import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.entity.Anonymous.AnonymousPostLike;
import com.community.backend.entity.Post.Comment;
import com.community.backend.entity.Post.Post;
import com.community.backend.entity.Post.PostLike;
import com.community.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final AnonymousPostRepository anonymousPostRepository;
    private final CommentRepository commentRepository;
    private final AnonymousCommentRepository anonymousCommentRepository;
    private final PostLikeRepository postLikeRepository;
    private final AnonymousPostLikeRepository anonymousPostLikeRepository;

    // 내가 쓴 일반 게시글
    public List<PostResponse> getMyPosts(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    // 내가 쓴 익명 게시글
    public List<AnonymousPostResponse> getMyAnonymousPosts(Long userId) {
        List<AnonymousPost> posts = anonymousPostRepository.findByWriterId(userId);
        return posts.stream()
                .map(AnonymousPostResponse::from)
                .collect(Collectors.toList());
    }

    // 내가 좋아요 누른 일반 게시글
    public List<PostResponse> getLikedPosts(Long userId) {
        List<PostLike> likes = postLikeRepository.findByUserId(userId);
        return likes.stream()
                .map(like -> PostResponse.from(like.getPost(), true))
                .collect(Collectors.toList());
    }

    // 내가 좋아요 누른 익명 게시글
    public List<AnonymousPostResponse> getLikedAnonymousPosts(Long userId) {
        List<AnonymousPostLike> likes = anonymousPostLikeRepository.findByUserId(userId);
        return likes.stream()
                .map(like -> AnonymousPostResponse.from(like.getPost(), true))
                .collect(Collectors.toList());
    }

    // 내가 댓글 단 일반 게시글 (댓글이 포함된 게시글 목록)
    public List<PostResponse> getCommentedPosts(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(comment -> PostResponse.from(comment.getPost()))
                .distinct()
                .collect(Collectors.toList());
    }

    // 내가 댓글 단 익명 게시글 (댓글이 포함된 게시글 목록)
    public List<AnonymousPostResponse> getCommentedAnonymousPosts(Long userId) {
        List<AnonymousComment> comments = anonymousCommentRepository.findByWriterId(userId);
        return comments.stream()
                .map(comment -> AnonymousPostResponse.from(comment.getPost()))
                .distinct()
                .collect(Collectors.toList());
    }
}
