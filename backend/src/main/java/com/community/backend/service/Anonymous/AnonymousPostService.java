package com.community.backend.service.Anonymous;

import com.community.backend.dto.Anonymous.AnonymousPostRequest;
import com.community.backend.dto.Anonymous.AnonymousPostResponse;
import com.community.backend.entity.Anonymous.AnonymousPost;
import com.community.backend.entity.Anonymous.AnonymousPostLike;
import com.community.backend.repository.AnonymousCommentRepository;
import com.community.backend.repository.AnonymousPostLikeRepository;
import com.community.backend.repository.AnonymousPostRepository;
import com.community.backend.repository.CommentRepository;
import com.community.backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnonymousPostService {

    private final AnonymousPostRepository postRepository;
    private final AnonymousPostLikeRepository postLikeRepository;
    private final S3Service s3Service;

    //주어진 id의 게시글을 조회
    public AnonymousPost findByIdOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }
    //게시글 조회, 해당 사용자의 좋아요 여부도 판단
    public AnonymousPostResponse getPostById(Long id, Long userId) {
        AnonymousPost post = findByIdOrThrow(id);
        boolean isLiked = userId != null && postLikeRepository.findByPostAndUserId(post, userId).isPresent();
        return AnonymousPostResponse.from(post, isLiked);
    }

    //모든게시글을 최신순으로 가져옴, 좋야요 눌렀는지 판단
    public List<AnonymousPostResponse> getAllPosts(Long userId) {
        return postRepository.findAllByOrderByUpdatedAtDescCreatedAtDesc().stream()
                .map(post -> {
                    boolean isLiked = userId != null && postLikeRepository.findByPostAndUserId(post, userId).isPresent();
                    return AnonymousPostResponse.from(post, isLiked);
                })
                .collect(Collectors.toList());
    }

    //게시글 저장
    public AnonymousPost savePost(AnonymousPostRequest dto,Long userId) {
        AnonymousPost post = new AnonymousPost();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setWriterId(userId);
        post.setWriterId(dto.getWriterId());
        post.setImageUrl(dto.getImageUrl());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    //해당 id의 게시글을 수정
    public void updatePost(Long id, AnonymousPostRequest dto, Long userId) {
        AnonymousPost post = findByIdOrThrow(id);

        if (!post.getWriterId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        if (post.getImageUrl() != null && dto.getImageUrl() != null &&
                !post.getImageUrl().equals(dto.getImageUrl())) {
            s3Service.deleteImage(post.getImageUrl());
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);
    }

    //해당 id의 게시글 삭제
    @Transactional
    public void deletePost(Long id, Long loginUserId) {
        AnonymousPost post = findByIdOrThrow(id);

        if (!post.getWriterId().equals(loginUserId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        // 이미지가 있다면 삭제
        if (post.getImageUrl() != null && !post.getImageUrl().isBlank()) {
            s3Service.deleteImage(post.getImageUrl());
        }

        // 댓글, 좋아요는 연관관계 + cascade 설정으로 자동 삭제됨
        postRepository.delete(post);
    }


    //해당 사용자의 좋아요 누름 여부 감시
    @Transactional
    public void toggleLike(Long postId, Long userId) {
        
        AnonymousPost post = findByIdOrThrow(postId);
        //해당유저가 좋아요를 눌렀는지 검사
        Optional<AnonymousPostLike> existing = postLikeRepository.findByPostAndUserId(post, userId);

        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            AnonymousPostLike like = new AnonymousPostLike();
            like.setPost(post);
            like.setUserId(userId);
            postLikeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
        }

        postRepository.save(post);
    }
}
