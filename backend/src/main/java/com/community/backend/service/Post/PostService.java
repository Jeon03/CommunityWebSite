package com.community.backend.service.Post;

import com.community.backend.dto.Post.PostRequest;
import com.community.backend.dto.Post.PostResponse;
import com.community.backend.entity.Post.BoardType;
import com.community.backend.entity.Post.Post;
import com.community.backend.entity.Post.PostLike;
import com.community.backend.repository.PostLikeRepository;
import com.community.backend.repository.PostRepository;
import com.community.backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final PostLikeRepository postLikeRepository;


    //단건조회
    public PostResponse getById(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        boolean isLiked = postLikeRepository.findByPostAndUserId(post, userId).isPresent();

        return PostResponse.from(post, isLiked);
    }

    public PostResponse create(PostRequest request, String writer, Long userId) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(writer)
                .userId(userId)
                .boardType(request.getBoardType())
                .imageUrl(request.getImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Post saved = postRepository.save(post);
        return PostResponse.from(saved, false);
    }

    public List<PostResponse> getByBoardType(BoardType type, Long userId) {
        return postRepository.findByBoardTypeOrderByUpdatedAtDescCreatedAtDesc(type)
                .stream()
                .map(post -> {
                    boolean isLiked = userId != null && postLikeRepository.findByPostAndUserId(post, userId).isPresent();
                    return PostResponse.from(post, isLiked);
                })
                .collect(Collectors.toList());
    }

    public boolean deletePost(Long postId, Long userId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty() || !postOpt.get().getUserId().equals(userId)) {
            return false; // 삭제 권한 없음
        }
        Post post = postOpt.get();

        if (post.getImageUrl() != null) {
            s3Service.deleteImage(post.getImageUrl());
        }
        postRepository.deleteById(postId);
        return true;
    }

    public PostResponse updatePost(Long id, PostRequest request, Long userId) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isEmpty()) return null;

        Post post = postOpt.get();
        if (!post.getUserId().equals(userId)) {
            return null;
        }
        
        //수정내용
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        
        
        //이미지수정처리
        if (request.getImageUrl() != null && !request.getImageUrl().equals(post.getImageUrl())) {
            if (post.getImageUrl() != null) {
                s3Service.deleteImage(post.getImageUrl());
            }
            post.setImageUrl(request.getImageUrl());
        }

        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);
        //종아요 여부 포함 반환
        boolean isLiked = postLikeRepository.findByPostAndUserId(post, userId).isPresent();
        return PostResponse.from(post, isLiked);
    }

    @Transactional
    public void toggleLike(Long postId, Long userId) {
        
        Post post = postRepository.findById(postId).orElseThrow();
        //현재사용자 좋아요 여부 확인
        Optional<PostLike> existing = postLikeRepository.findByPostAndUserId(post, userId);

        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUserId(userId);
            postLikeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
        }

        postRepository.save(post);
    }


}
