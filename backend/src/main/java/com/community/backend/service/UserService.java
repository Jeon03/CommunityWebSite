package com.community.backend.service;


import com.community.backend.dto.Login.SignupRequest;
import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.entity.Post.Post;
import com.community.backend.entity.User;
import com.community.backend.repository.*;
import com.community.backend.service.Email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final AnonymousPostRepository anonymousPostRepository;
    private final AnonymousCommentRepository anonymousCommentRepository;
    private final AnonymousPostLikeRepository anonymousPostLikeRepository;

    public void registerUser(SignupRequest request) {
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        if (!emailService.isVerified(request.getEmail())) {
            throw new IllegalArgumentException("이메일 인증이 필요합니다.");
        }
        
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //유저객체 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserAndData(Long userId) {
        // 일반 게시글 이미지 삭제
        List<Post> posts = postRepository.findByUserId(userId);
        for (Post post : posts) {
            s3Service.deleteImage(post.getImageUrl());
        }

        // 익명 게시글 이미지 삭제
        List<AnonymousPost> anonymousPosts = anonymousPostRepository.findByWriterId(userId);
        for (AnonymousPost post : anonymousPosts) {
            s3Service.deleteImage(post.getImageUrl());
        }

        // 연관 데이터 삭제 순서
        postLikeRepository.deleteByUserId(userId);
        commentRepository.deleteByUserId(userId);
        postRepository.deleteByUserId(userId);

        anonymousPostLikeRepository.deleteByUserId(userId);
        anonymousCommentRepository.deleteByWriterId(userId);
        anonymousPostRepository.deleteByWriterId(userId);

        // 사용자 삭제
        userRepository.deleteById(userId);
    }

}
