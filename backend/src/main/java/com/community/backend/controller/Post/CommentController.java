package com.community.backend.controller.Post;

import com.community.backend.dto.Post.CommentRequest;
import com.community.backend.dto.Post.CommentResponse;
import com.community.backend.entity.Post.Comment;
import com.community.backend.entity.User;
import com.community.backend.repository.CommentRepository;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.Post.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication authentication) {
        if (authentication == null) return null;
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    // 댓글 목록 조회
    @GetMapping
    public List<CommentResponse> getCommentsByPostId(@RequestParam Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // 댓글 등록
    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody CommentRequest request) {
        Comment saved = commentService.createComment(request);
        return ResponseEntity.ok(saved);
    }

    // 댓글 삭제 (작성자 확인)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        commentService.deleteComment(id, userId);
        return ResponseEntity.noContent().build();
    }
}