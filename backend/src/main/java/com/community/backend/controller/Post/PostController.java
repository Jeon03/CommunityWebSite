package com.community.backend.controller.Post;


import com.community.backend.SecurityConfig.JwtTokenProvider;
import com.community.backend.dto.Post.PostRequest;
import com.community.backend.dto.Post.PostResponse;
import com.community.backend.entity.User;
import com.community.backend.entity.Post.BoardType;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.Post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final JwtTokenProvider jwtTokenProvider;
    private final PostService postService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(Authentication authentication) {
        if (authentication == null) return null;
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElse(null);
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<PostResponse>> getByBoardType(@RequestParam("type") BoardType type,
                                                             Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(postService.getByBoardType(type, userId));
    }

    //게시글 상세
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long postId,
                                                Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(postService.getById(postId, userId));
    }

    @PostMapping("/create")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request,
                                                   Authentication authentication, HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(authentication);
        String token = jwtTokenProvider.resolveToken(httpRequest);
        String writer = jwtTokenProvider.getNickname(token);

        return ResponseEntity.ok(postService.create(request, writer, userId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable Long postId,
                                              Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(postService.deletePost(postId, userId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                                   @RequestBody PostRequest request,
                                                   Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(postService.updatePost(postId, request, userId));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId,
                                           Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        postService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }
}

