package com.community.backend.controller.Anonymous;

import com.community.backend.dto.Anonymous.AnonymousCommentRequest;
import com.community.backend.dto.Anonymous.AnonymousCommentResponse;
import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.entity.User;
import com.community.backend.repository.AnonymousPostRepository;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.Anonymous.AnonymousCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anonymous-comments")
@RequiredArgsConstructor
public class AnonymousCommentController {

    private final AnonymousCommentService anonymousCommentService;
    private final UserRepository userRepository;
    private final AnonymousPostRepository anonymousPostRepository;

    private Long getCurrentUserId(Authentication authentication) {
        if (authentication == null) return null;
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody AnonymousCommentRequest dto, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        anonymousCommentService.createComment(dto, userId);
        return ResponseEntity.ok().build();
    }


    //댓글 조회
    @GetMapping
    public ResponseEntity<List<AnonymousCommentResponse>> getComments(@RequestParam Long postId) {
        AnonymousPost post = anonymousPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return ResponseEntity.ok(anonymousCommentService.getCommentDtosByPost(post));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        anonymousCommentService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
