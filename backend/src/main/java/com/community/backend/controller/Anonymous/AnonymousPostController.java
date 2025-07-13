package com.community.backend.controller.Anonymous;

import com.community.backend.dto.Anonymous.AnonymousPostRequest;
import com.community.backend.dto.Anonymous.AnonymousPostResponse;
import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.entity.User;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.Anonymous.AnonymousPostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anonymous-posts")
@RequiredArgsConstructor
public class AnonymousPostController {

    private final AnonymousPostService postService;
    private final UserRepository userRepository;

    // userId 추출 메서드
    private Long getCurrentUserId(Authentication authentication) {
        if (authentication == null) return null;
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    // 글 등록
    @PostMapping
    public ResponseEntity<AnonymousPostResponse> createPost(@RequestBody AnonymousPostRequest dto,
                                                            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        AnonymousPost saved = postService.savePost(dto, userId);
        return ResponseEntity.ok(AnonymousPostResponse.from(saved, false));
    }

    // 전체 목록
    @GetMapping("/all")
    public ResponseEntity<List<AnonymousPostResponse>> getAll(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(postService.getAllPosts(userId));
    }

    // 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<AnonymousPostResponse> getPost(@PathVariable Long id, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(postService.getPostById(id, userId));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id,
                                           @RequestBody AnonymousPostRequest dto,
                                           Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        postService.updatePost(id, dto, userId);
        return ResponseEntity.ok().build();
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        postService.deletePost(id, userId);
        return ResponseEntity.ok().build();
    }

    // 좋아요 토글
    @PutMapping("/{postId}/like")
    public ResponseEntity<Void> toggleAnonymousLike(@PathVariable Long postId, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        postService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }
}

