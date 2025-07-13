package com.community.backend.controller;

import com.community.backend.dto.Anonymous.AnonymousPostResponse;
import com.community.backend.dto.Post.CommentResponse;
import com.community.backend.dto.Post.PostResponse;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication auth) {
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."))
                .getId();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getMyPosts(Authentication auth) {
        return ResponseEntity.ok(myPageService.getMyPosts(getCurrentUserId(auth)));
    }

    @GetMapping("/anonymous-posts")
    public ResponseEntity<List<AnonymousPostResponse>> getMyAnonymousPosts(Authentication auth) {
        return ResponseEntity.ok(myPageService.getMyAnonymousPosts(getCurrentUserId(auth)));
    }

    @GetMapping("/liked-posts")
    public ResponseEntity<List<PostResponse>> getLikedPosts(Authentication auth) {
        return ResponseEntity.ok(myPageService.getLikedPosts(getCurrentUserId(auth)));
    }

    @GetMapping("/liked-anonymous-posts")
    public ResponseEntity<List<AnonymousPostResponse>> getLikedAnonymousPosts(Authentication auth) {
        return ResponseEntity.ok(myPageService.getLikedAnonymousPosts(getCurrentUserId(auth)));
    }

    @GetMapping("/commented-posts")
    public ResponseEntity<List<PostResponse>> getCommentedPosts(Authentication auth) {
        return ResponseEntity.ok(myPageService.getCommentedPosts(getCurrentUserId(auth)));
    }

    @GetMapping("/commented-anonymous-posts")
    public ResponseEntity<List<AnonymousPostResponse>> getCommentedAnonymousPosts(Authentication auth) {
        return ResponseEntity.ok(myPageService.getCommentedAnonymousPosts(getCurrentUserId(auth)));
    }
}
